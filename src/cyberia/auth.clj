(ns cyberia.auth
  (:require [cheshire.core :refer [parse-string]]
            [clj-http.client :as client]
            [cyberia.post :refer [ACTIVITY-PUB-TYPE]]
            [cyberia.utils :refer [authorization-header]]))

(def DEFAULT-SCOPE "read write")
(def DEFAULT-REDIRECT-URI "urn:ietf:wg:oauth:2.0:oob")
(def DEFAULT-RESPONSE-TYPE "code")

(defn make-pleroma-oauth2
  [url]
  {:base-url url
   :app-uri (str url "/api/v1/apps")
   :authorization-uri (str url "/oauth/authorize")
   :access-token-uri (str url "/oauth/token")
   :redirect-uri DEFAULT-REDIRECT-URI
   :client-id nil
   :client-secret nil
   :access-query-param :access_token
   :scope DEFAULT-SCOPE
   :response-type DEFAULT-RESPONSE-TYPE
   :grant-type "authorization_code"
   :authorization-code nil})

(defn make-app-request
  [oauth-map]
  (let [{:keys [app-uri redirect-uri scope]} oauth-map]
    (parse-string (:body (client/post
                          app-uri
                          {:form-params {:client_name "Cyberia"
                                         :redirect_uris redirect-uri
                                         :scopes scope}
                           :content-type :json}))
                  true)))

(defn register-app
  [oauth-map]
  (let [app-response (make-app-request oauth-map)]
    (merge oauth-map {:client-id (:client_id app-response)
                      :client-secret (:client_secret app-response)})))

(defn create-oauth2-authorization-uri
  [oauth-map]
  (let [{:keys [authorization-uri
                client-id
                redirect-uri
                response-type
                scope]}
        oauth-map]
    (str
     authorization-uri
     "?"
     (client/generate-query-string {"client_id" client-id
                                    "redirect_uri" redirect-uri
                                    "response_type" response-type
                                    "scope" scope}))))

(defn get-oauth2-authorization-code
  [oauth-map]
  (let [uri (create-oauth2-authorization-uri oauth-map)]
    (println (str "Approve request at: " uri))
    (println "Paste token: ")
    (->> {:authorization-code (read-line)}
         (merge oauth-map))))

(defn get-token
  [oauth-map]
  (let [{:keys [access-token-uri
                grant-type
                client-id
                client-secret
                redirect-uri
                authorization-code
                scope]}
        oauth-map]
    (parse-string (:body (client/post
                          access-token-uri
                          {:form-params {:grant_type grant-type
                                         :client_id client-id
                                         :client_secret client-secret
                                         :redirect_uri redirect-uri
                                         :code authorization-code
                                         :scopes scope}
                           :content-type :json}))
                  true)))

(defn get-user-info
  [token-map]
  (let [user (parse-string (:body (client/get
                                   (:me token-map)
                                   {:headers (authorization-header token-map)
                                    :content-type ACTIVITY-PUB-TYPE
                                    :accept ACTIVITY-PUB-TYPE})))]
    (assoc token-map :user user)))

(defn get-credentials
  [url]
  (-> (make-pleroma-oauth2 url)
      (register-app)
      (get-oauth2-authorization-code)
      (get-token)
      (get-user-info)))