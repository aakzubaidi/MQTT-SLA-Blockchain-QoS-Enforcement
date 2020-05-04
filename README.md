# MQTT-SLA-Blockchain-QoS-Enforcement
The use of Blockchain to enforce SLA related to IoT components: Here is a use-case of MQTT SLA established by Google Cloud Platform IoT core. We use Hyperledger Fabric as underlying network and enforce the SLA using a smart contract written in Java.

Caliper
CALIPER_FABRIC_SKIPCREATECHANNEL_MYCHANNEL=true
npx caliper launch master \
    --caliper-bind-sut fabric:1.4.6 \
    --caliper-workspace . \
    --caliper-benchconfig benchmarks/scenario/simple/config_long.yaml \
    --caliper-networkconfig /Users/aliaalzubaidi/caliper-benchmarks/networks/fabric/fabric-v1.4.6/2org1peercouchdb_raft/fabric-go-tls.yaml \ 
    --caliper-fabric-timeout-invokeorquery=300000
