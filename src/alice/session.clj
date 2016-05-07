(ns alice.session)

(defn authenticate
  [response secret]
  (assoc-in response [:session :secret] secret))

(defn deauthenticate
  [response]
  (assoc-in response [:session :secret] nil))

(defn secret
  [request]
  (get-in request [:session :secret]))

(defn authenticated?
  [request]
  (boolean (secret request)))
