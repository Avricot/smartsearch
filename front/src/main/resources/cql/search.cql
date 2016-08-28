CREATE KEYSPACE IF NOT EXISTS smartsearch
    WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
    AND DURABLE_WRITES = true;


CREATE TABLE IF NOT EXISTS smartsearch.client (
    id uuid,
    name text,
    referers set<text>,
    PRIMARY KEY(id));

insert into smartsearch.client (id, name, referers) VALUES (c15cb8d0-6d1c-11e6-a74e-716b79df3afe, 'quentin', {'localhost', 'null'});

CREATE TABLE IF NOT EXISTS smartsearch.search (
    clientId uuid,
    searchType text,
    periodPartition bigint,
    serverPartition int,
    searchTime timeuuid,
    content blob,
    PRIMARY KEY((clientId, searchType, periodPartition, serverPartition), searchTime));


-- (clientId, searchType, periodPartition, serverPartition) => Partition key (id d'une row), hotspot si bcp de recherche, on ajoute un 'serverPartition int [1->n]' pour partitioner entre n serveurs. Ne devrait pas dépasser 100k colonnes => utilise periodPartition : timestamp arrondi à X milliseconde (par exemple 1 partition = 1jour)
-- (searchTime) => clustering column => (prefixe pour un groupe de cellule). Les column d'une même row peuvent être séparés en plusieurs SSTables (d'ou l'utilisation de bloomfilter). Si bcp de column (>column_index_size_in_kb=64k) un index est créé pour parcourir + vite les colonnes au sein d'une meme rows.



