from NetworkXSingleImplementation.ParDISTExampleGraphGenerator import createParDISTExampleGraph
from NetworkXSingleImplementation.Preprocessor import preprocess
from NetworkXSingleImplementation.IDT import IDT
from NetworkXSingleImplementation.helpers import get_partition_names
import networkx as nx
import math


def findDistance(cdm_matrix, graph, extendedComponentList, sourceNode, targetNode):
    distance = -math.inf

    sourceNodePartition = graph.nodes[sourceNode]['partition']
    targetNodePartition = graph.nodes[targetNode]['partition']

    # if source and destination is in same partition run dijkstra
    if sourceNodePartition == targetNodePartition:
        extendedComponent = extendedComponentList[sourceNodePartition]
        distance = nx.dijkstra_path_length(extendedComponent, sourceNode, targetNode, weight='weight')
    # sourceNode and destinationNode are in different component
    else:
        # we use graph for just getting IDT of source and target node
        sourceNode_IDT = graph.nodes[sourceNode]['IDT']
        targetNode_IDT = graph.nodes[targetNode]['IDT']

        # get index from global partitionIndexList
        sourceIndex = next(partitionTuple[0] for partitionTuple in partitionIndexList if partitionTuple[1] == sourceNodePartition)
        targetIndex = next(partitionTuple[0] for partitionTuple in partitionIndexList if partitionTuple[1] == targetNodePartition)

        componentDistance = cdm_matrix[sourceIndex][targetIndex]
        print(componentDistance)

        tempComponentDistance = []

        # join source IDT and componentDistance
        for IDT_element in sourceNode_IDT.idtList:
            for componentDistance_element in componentDistance:
                if IDT_element['borderNode'] == componentDistance_element[0]:
                    tempComponentDistance.append(
                        (componentDistance_element[0], componentDistance_element[1], componentDistance_element[2] + IDT_element['distance'])
                    )

        print(tempComponentDistance)

        finalComponentDistance = []

        # join tempExtendedComponent with target IDT
        for IDT_element in targetNode_IDT.idtList:
            for tempComponentDistance_element in tempComponentDistance:
                if IDT_element['borderNode'] == tempComponentDistance_element[1]:
                    finalComponentDistance.append(
                        (tempComponentDistance_element[0], tempComponentDistance_element[1], tempComponentDistance_element[2] + IDT_element['distance'])
                    )

        print(finalComponentDistance)

        # return shortest value in finalComponentDistance
        minDistanceTuple = min(finalComponentDistance, key = lambda t : t[2])
        print(minDistanceTuple)

        return minDistanceTuple

def findShortestPath(cdm_matrix, graph, transitNetowork, extendedComponentList, sourceNode, targetNode):
    shortestPath = []

    sourceNodePartition = graph.nodes[sourceNode]['partition']
    targetNodePartition = graph.nodes[targetNode]['partition']

    # if sourceNode and targetNode are in same partition run dijkstra
    if sourceNodePartition == targetNodePartition:
        extendedComponent = extendedComponentList[sourceNodePartition]
        shortestPath = nx.dijkstra_path(extendedComponent, sourceNode, targetNode, weight='weight')
    else:
        # first find which border nodes you need to use, for this you can direclty use findDistance, it will give you
        # it will give you bordernode for partitionA, bordernode for partitionB and distance
        shortestDistanceTuple = findDistance(cdm_matrix, graph, extendedComponentList, sourceNode, targetNode)
        borderNodeForSource = shortestDistanceTuple[0]
        borderNodeForTarget = shortestDistanceTuple[1]

        print(borderNodeForSource, borderNodeForTarget)
        # you know shortest path from source node to borderNodeForSource it's in IDT
        # you know shortest path from destinatoin node to borderNodeForTarget it's in IDT
        # need to run dijkstra in transitNetowrk, source -> borderNodeSource, target -> borderNodeTarget
        sourceNode_IDT = graph.nodes[sourceNode]['IDT']
        targetNode_IDT = graph.nodes[targetNode]['IDT']

        s1 = next(idtDict['path'] for idtDict in sourceNode_IDT.idtList if idtDict['borderNode'] == borderNodeForSource)
        s2 = next(idtDict['path'] for idtDict in targetNode_IDT.idtList if idtDict['borderNode'] == borderNodeForTarget)
        # anlami kaybolmasin diye listeyi ters cevir
        # cunku s2 target node tan border node a gore duzenlenmis, biz ters cevircez
        s2.reverse()

        # transportNetwork u kullanarak iki border node arasindaki shortest pathi bulacan
        s3 = nx.dijkstra_path(transitNetwork, borderNodeForSource, borderNodeForTarget, weight='weight')

        fullShortestPath = s1
        fullShortestPath.extend(node for node in s3 if node not in fullShortestPath)
        fullShortestPath.extend(node for node in s2 if node not in fullShortestPath)

    return fullShortestPath





partitionIndexList = []

if __name__ == "__main__":
    G = createParDISTExampleGraph()

    partitionsSet = get_partition_names(graph=G)
    partitions = list(partitionsSet)
    partitions.sort()

    for index, partition in enumerate(partitions):
        partitionIndexList.append((index, partition))

    transitNetwork = nx.Graph()

    # preprocess returns extendedcomponentlist which includes extended components for all components
    # and cmd matrix
    resultDict = preprocess(graph=G, transitNetwork=transitNetwork, partitions=partitions)
    print(resultDict)

    cdm_matrix = resultDict['cdm_matrix']
    extendedComponentList = resultDict['extendedComponentList']
    # n0 ile n20 arasindaki distance i bulalim.
    # returns tuple sourceNode, targetNode, minDistance
    minDistance = findDistance(cdm_matrix, G, extendedComponentList, 'n0', 'n20')[2]
    shortestPath = findShortestPath(cdm_matrix, G, transitNetwork, extendedComponentList, 'n0', 'n20')






