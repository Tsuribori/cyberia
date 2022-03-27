(ns cyberia.post 
  (:require [cheshire.core :refer [generate-string]]
            [clj-http.client :as client]
            [cyberia.utils :refer [get-user]]
            [clojure.spec.alpha :as s]))

(def ACTIVITY-PUB-TYPE "application/activity+json")
(def ACTIVITY-PUB-CONTEXT "https://www.w3.org/ns/activitystreams")
(def ACTIVITY-PUB-PUBLIC "https://www.w3.org/ns/activitystreams#Public")

(s/def :ap/attachment string?)
(s/def :ap/context string?)
(s/def :ap/attributedTo string?)
(s/def :ap/content string?)
(s/def :ap/to (s/coll-of string? :kind vector?))
(s/def :ap/type string?)
(s/def :ap/published string?)


(s/def :ap/object (s/keys :req-un [:ap/published
                                :ap/type
                                :ap/content
                                :ap/to]
                          :opt-un [:ap/attachment]))

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

(defn create-note
  [^String content]
  {:post [(s/valid? :ap/activity %)]}
  {:type "Create"
   :context ACTIVITY-PUB-CONTEXT
   :object {:type "Note"
            :to [ACTIVITY-PUB-PUBLIC]
            :content content
            :published (now)}})

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
     (str (:me user) "/outbox")
     {:headers {"Authorization" (str "Bearer " (:access_token user))}
      :content-type ACTIVITY-PUB-TYPE
      :accept ACTIVITY-PUB-TYPE
      :body (flight-transform activity)})))