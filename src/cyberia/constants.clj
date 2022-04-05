(ns cyberia.constants)

(def DEFAULT-SCOPE "read write")
(def DEFAULT-REDIRECT-URI "urn:ietf:wg:oauth:2.0:oob")
(def DEFAULT-RESPONSE-TYPE "code")

(def ACTIVITY-PUB-TYPE "application/activity+json")
(def ACTIVITY-PUB-CONTEXT "https://www.w3.org/ns/activitystreams")
(def ACTIVITY-PUB-PUBLIC "https://www.w3.org/ns/activitystreams#Public")

(def DEFAULT-CONFIG-DIR "~/.cyberia")
(def DEFAULT-CRED-FILE (str DEFAULT-CONFIG-DIR "/default.json"))
(def DEFAULT-APP-PREFIX "CyberiaClient-")