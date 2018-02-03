(ns pb-parse.core
  (:require [flatland.protobuf.core :as p]
            [clojure.string :as s]
            )
  (:import (caffe.Caffe))
  (:gen-class))



(defn read-lines-from [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (line-seq rdr))
  )

(defn value-of [str]
  (let [v  (re-find #"\".*\"" str)]
    (if (nil?  v)
      (read-string str)
      v)
    )
  )

(defn parse-kv [line]
  (let [[k v] (map s/trim  (s/split line #":"))]
    (if (and  (not (empty? k)) (not (empty? v))) 
      {(keyword k) (value-of  v)}
      nil
      )))

(defn parse-key [line]
  (let [[k] (map s/trim  (s/split line #"\{"))]
    k
    ))


(defn sub-begin? [line]
  (let [v  (re-find #".*\{.*" line)]
    (if (not  (nil?  v))
      true
      nil)
    )
  )


(defn sub-end? [line]
  (let [v  (re-find #".*\}" line)]
    (if (not  (nil?  v))
      true
      nil)
    )
  )


(defn get-struct-lines [lines]
  (loop [count 1
         p (first lines)
         q (rest lines)
         r '()
         ]
    (if (= count 0)
      r
      (if (sub-begin? p)
        (recur (inc count) (first q) (rest q) (concat r (list p)))
        (if (sub-end? p)
          (recur (dec count) (first q) (rest q) (concat r (list p)) )
          (recur count (first q) (rest q) (concat r (list p)))
          )
        ))
    )
  )

(defn parse-line [lines]
  (let [line (first lines)
        r (rest lines)]
    (if (not  (nil? line))
      (if (sub-end? line)
        (parse-line r)
        (let [kv (parse-kv line)]
          (if (nil? kv) 
            {(keyword (parse-key line)) (parse-line r)}
            kv)))
      ))
  )

(defn parse-struct [key lines]
  (loop [k key
         line (first lines)
         r (rest lines)
         lastkey nil
         rt  {}
         ]
    (let [
          kv (parse-kv line)
          ck (if (and kv  (= lastkey (first  (keys kv))))
               (assoc rt lastkey (vector (lastkey rt) (lastkey kv)))
               kv
               )
          ]
      (if (empty? r)
        {(keyword k) rt}
        (if (nil? kv)
          ;; (recur (parse-key line) (first r) (get-struct-lines r) nil rt)
          (let [subs (get-struct-lines r)]
            
            )
          (recur k (first r) (rest r) (first  (keys kv)) (conj rt  ck))
          )
        )))
  )

(defn parse [lines]
  (loop [
         line (first lines)
         kv (parse-kv line)
         r (rest lines)
         rt '()
         ]
    (if (empty? r)
      (concat rt (list kv))
      
      )
    )
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
