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
                     (get-credentials (first (:_arguments opts))))]
    (io/make-parents (cred-file))
    (spit (cred-file) credentials)
    (println "Logged in succesfully!")))


(defn post!
  [opts]
  (let [post (first (:_arguments opts))]
    (post-object (create-note post opts))
    (println "Posted succesfully!")))

(def CONFIGURATION
  {:app         {:command     "cyberia"
                 :description "Pleroma CLI posting."
                 :version     "0.1.0"}
   :commands    [{:command     "login" :short "l"
                  :description ["Obtain Pleroma auth token. Takes URL of server as argument."]
                  :type        :string
                  :runs        login!}
                 {:command     "post" :short "p"
                  :description ["Post to Pleroma. Takes a string as argument."]
                  :type        :string
                  :opts        [{:as "File to attach to post."
                                 :type :string
                                 :option "file"
                                 :short "f"}
                                {:as "Mark post as sensitive."
                                 :type :with-flag
                                 :option "sensitive"
                                 :short "s"}]
                  :runs        post!}]})

(defn -main
  [& args]
  (run-cmd args CONFIGURATION))
