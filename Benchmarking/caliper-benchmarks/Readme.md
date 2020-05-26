
The benchmarking folder contrains the needed configuration for replicating the performance evaluation.

##  Hyperledger Caliper

[Follow the instructions running Hyperledger Caliper](https://hyperledger.github.io/caliper/)

## HLF network options

### Using Hyperledger Caliper to create a simple HLF network 
This option is useful for testing on the fly. 

Go to subdirectory:

    /caliper-benchmarks/network/fabric-v1.4.6/Fabric/

There is a Yaml file inside 'java-2o2p3oldb.yaml' that can compose and lanuch a network of 2 orgnisations, one peer for each, 2 orderers
#
## Testing an existing HLF Fabric
A representative YAML file is 



### Tesing using basic HLF network


    npx caliper launch master \
    --caliper-bind-sut fabric:1.4.6 \
    --caliper-workspace . \
    --caliper-benchconfig benchmarks/scenario/simple/config_long.yaml \
    --caliper-networkconfig /Users/aliaalzubaidi/caliper-benchmarks/networks/fabric/fabric-v1.4.6/2org1peercouchdb_raft/fabric-go-tls.yaml \ 
    --caliper-fabric-timeout-invokeorquery=300000


### Testing by adapting to existing HLF network (First network)

make use you to run the following command first, in order to skip creating a channel.

     CALIPER_FABRIC_SKIPCREATECHANNEL_MYCHANNEL=true

Following, you can start testing using the following commands:

    npx caliper launch master \
    --caliper-bind-sut fabric:1.4.6 \
    --caliper-workspace . \
    --caliper-benchconfig benchmarks/scenario/simple/config_long.yaml \
    --caliper-networkconfig /Users/aliaalzubaidi/caliper-benchmarks/networks/fabric/fabric-v1.4.6/2org1peercouchdb_raft/fabric-go-tls.yaml \ 
    --caliper-fabric-timeout-invokeorquery=300000

An HTML-formatted report will be generated and placed in the current folder, pointed by the terminal.