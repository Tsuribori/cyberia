(ns cyberia.core
  (:require [cyberia.auth :refer [DEFAULT-CRED-FILE get-credentials]]
            [cli-matic.core :refer [run-cmd]]
            [cheshire.core :refer [generate-string]]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:gen-class))

(defn expand-home
  [s]
  (if (.startsWith s "~")
    (string/replace-first s "~" (System/getProperty "user.home"))
    s))

(defn login!
  [opts]
  (let [credentials (generate-string
                     (get-credentials (:url opts)))
        full-path (expand-home DEFAULT-CRED-FILE)]
    (io/make-parents full-path)
    (spit full-path credentials)
    (println "Logged in succesfully!")))


(def CONFIGURATION
  {:app         {:command     "cyberia"
                 :description "Pleroma CLI posting."
                 :version     "0.1.0"}
   :commands    [{:command     "login" :short "l"
                  :description ["Obtain Pleroma auth token"]
                  :opts        [{:option "url" :short "u" :as "URL of Pleroma server" :type :string}]
                  :runs        login!}]})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-cmd args CONFIGURATION))
