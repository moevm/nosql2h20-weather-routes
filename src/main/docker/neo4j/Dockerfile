FROM neo4j:4.0.0

ARG GDS=neo4j-graph-data-science-1.4.1-standalone.zip
ARG GDS_URL=https://s3-eu-west-1.amazonaws.com/com.neo4j.graphalgorithms.dist/graph-data-science/${GDS}

ENV NEO4JLABS_PLUGINS='["apoc"]'
ENV NEO4J_apoc_export_file_enabled=true
ENV NEO4J_apoc_import_file_enabled=true
ENV NEO4J_apoc_import_file_use__neo4j__config=true

RUN apt-get update \
    && apt-get install -y curl unzip \
    && cd plugins \
    && curl -O ${GDS_URL} \
    && unzip ${GDS} \
    && rm ${GDS} \
    && apt-get -y purge --auto-remove curl unzip \
    && rm -rf /tmp/* \
    && rm -rf /var/lib/apt/lists/*

COPY --chown=neo4j neo4j.conf conf/
