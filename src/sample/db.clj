(ns sample.db)

(def db (or (System/getenv "DATABASE_PARSER")
            "postgresql://localhost:5432/parser"))
(println "Using database connection URL:" db)