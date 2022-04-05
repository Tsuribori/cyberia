(ns cyberia.utils
  (:require [cheshire.core :refer [parse-string]]
            [clojure.string :as string]
            [cyberia.constants :refer [DEFAULT-APP-PREFIX DEFAULT-CRED-FILE]]))

(defn expand-home
  [s]
  (if (.startsWith s "~")
    (string/replace-first s "~" (System/getProperty "user.home"))
    s))

(defn cred-file
  []
  (expand-home DEFAULT-CRED-FILE))

(defn get-user
  []
  (let [file-contents (slurp (cred-file))]
    (parse-string file-contents true)))

(defn authorization-header
  [user]
  {"Authorization" (str "Bearer " (:access_token user))})

(defn parse-body
  [response]
  (parse-string (:body response) true))

(defn get-system-info
  []
  (let [sys-fun #(System/getProperty %)]
    (str (sys-fun "os.name")
         (sys-fun "os.version"))))

(defn get-app-name
  []
  (str DEFAULT-APP-PREFIX (get-system-info)))