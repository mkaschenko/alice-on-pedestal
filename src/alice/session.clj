(ns alice.session)

(defn authenticate
  [response secret]
  (assoc-in response [:session :secret] secret))

(defn deauthenticate
  [response]
  (assoc-in response [:session :secret] nil))

(defn authenticated?
  [request]
  (boolean (get-in request [:session :secret])))
