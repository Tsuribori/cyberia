(ns cyberia.core
  (:require [cheshire.core :refer [generate-string]]
            [cli-matic.core :refer [run-cmd]]
            [clojure.java.io :as io]
            [cyberia.auth :refer [get-credentials]]
            [cyberia.post :refer [create-note post-object]]
            [cyberia.utils :refer [cred-file]])
  (:gen-class))

(defn login!
  [opts]
  (let [credentials (generate-string
                     (get-credentials (:url opts)))]
    (io/make-parents (cred-file))
    (spit (cred-file) credentials)
    (println "Logged in succesfully!")))


(defn post!
  [opts]
  (let [post (first (:_arguments opts))]
    (post-object (create-note post))
    (println "Posted succesfully!")))

(def CONFIGURATION
  {:app         {:command     "cyberia"
                 :description "Pleroma CLI posting."
                 :version     "0.1.0"}
   :commands    [{:command     "login" :short "l"
                  :description ["Obtain Pleroma auth token"]
                  :opts        [{:option "url" :short "u" :as "URL of Pleroma server" :type :string}]
                  :runs        login!}
                 {:command     "post" :short "p"
                  :description ["Post to Pleroma"]
                  :runs        post!}]})

(defn -main
  [& args]
  (run-cmd args CONFIGURATION))
