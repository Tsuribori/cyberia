(ns cyberia.post 
  (:require [cheshire.core :refer [generate-string parse-string]]
            [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [cyberia.utils :refer [authorization-header get-user]]))

(def ACTIVITY-PUB-TYPE "application/activity+json")
(def ACTIVITY-PUB-CONTEXT "https://www.w3.org/ns/activitystreams")
(def ACTIVITY-PUB-PUBLIC "https://www.w3.org/ns/activitystreams#Public")

(s/def :ap/attachment-map (s/map-of keyword? any?))
(s/def :ap/attachment (s/coll-of :ap/attachment-map))
(s/def :ap/context string?)
(s/def :ap/attributedTo string?)
(s/def :ap/content string?)
(s/def :ap/to (s/coll-of string? :kind vector?))
(s/def :ap/type string?)
(s/def :ap/published string?)
(s/def :ap/sensitive boolean?)


(s/def :ap/object (s/keys :req-un [:ap/published
                                   :ap/type
                                   :ap/content
                                   :ap/to]
                          :opt-un [:ap/attachment
                                   :ap/sensitive]))

(s/def :ap/activity (s/keys :req-un [:ap/context
                                     :ap/type
                                     :ap/object]
                            :opt-un [:ap/to]))

(defn now
  []
  (-> (java.time.Instant/now)
      (.truncatedTo
       (java.time.temporal.ChronoUnit/SECONDS))
      (.toString)))

(defn post-file
  [file]
  (when file
    (let [user (get-user)]
      (parse-string (:body (client/post
                            (get-in user [:user :endpoints :uploadMedia])
                            {:headers (authorization-header user)
                             :multipart [{:name "file"
                                          :content (io/file file)}]}))
                    true))))

(defn create-note
  [^String content opts]
  {:post [(s/valid? :ap/activity %)]}
  (let [{file :file
         sensitive :sensitive
         :or {sensitive false}}
        opts]
    {:type "Create"
     :context ACTIVITY-PUB-CONTEXT
     :object {:type "Note"
              :to [ACTIVITY-PUB-PUBLIC]
              :content content
              :published (now)
              :attachment [(post-file file)]
              :sensitive sensitive}}))

(defn flight-transform
  [activity]
  {:pre [(s/valid? :ap/activity activity)]}
  (let [context (:context activity)]
    (-> activity
        (dissoc :context)
        (assoc (keyword "@context") context)
        (generate-string))))

(defn post-object
  [activity]
  {:pre [(s/valid? :ap/activity activity)]}
  (let [user (get-user)]
    (client/post
     (get-in user [:user :outbox])
     {:headers (authorization-header user)
      :content-type ACTIVITY-PUB-TYPE
      :accept ACTIVITY-PUB-TYPE
      :body (flight-transform activity)})))