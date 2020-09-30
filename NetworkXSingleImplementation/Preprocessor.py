from NetworkXSingleImplementation.ParDISTExampleGraphGenerator import createParDISTExampleGraph
from NetworkXSingleImplementation.IDT import IDT
import networkx as nx




def create_IDT(graph, partitionMembers):
    nonBorderNodes = []
    borderNodes = []

    # seperate border and non-border nodes
    for member in partitionMembers:
        if member[1]['borderNode'] == False:
            nonBorderNodes.append(member)
        else:
            borderNodes.append(member)

    # for each non-border node run dijkstra to border nodes
    for nonBorderNode in nonBorderNodes:
        nonBorderNodeName = nonBorderNode[0]
        idt = IDT(nonBorderNodeName)
        for borderNode in borderNodes:
            borderNodeName = borderNode[0]
            distance = nx.dijkstra_path_length(graph, nonBorderNodeName, borderNodeName, weight='weight')
            path = nx.dijkstra_path(graph, nonBorderNodeName, borderNodeName, weight='weight')
            distanceAndPathDict = {'borderNode': borderNodeName, 'distance': distance, 'path': path}
            idt.idtList.append(distanceAndPathDict)

        # add idt to node in networkx
        graph.nodes[nonBorderNodeName]['IDT'] = idt


def getEdgeCouples(shortestPath):
    firstIndex = 0
    secondIndex = 2
    listLength = len(shortestPath)
    listOfEdgeCouples = []
    while secondIndex <= listLength:
        # print("shortest path nodes -> ", shortestPath[firstIndex:secondIndex])
        listOfEdgeCouples.append((tuple(shortestPath[firstIndex:secondIndex])))
        firstIndex = firstIndex + 1
        secondIndex = secondIndex + 1
    return listOfEdgeCouples


def get_all_nodes_in_path(graph, shortestPathList):
    # all unique node names in shortestPathList
    nodes = set()
    nodesWithAttr = []

    # all unique edge pairs in shortestPathList
    edges = set()
    edgesWithAttr = []

    for shortestPath in shortestPathList:
        for node in shortestPath[2]:
            nodes.add(node)

        listOfEdgeCouples = getEdgeCouples(shortestPath[2])
        for edgePair in listOfEdgeCouples:
            edges.add(edgePair)

    for node in nodes:
        nodeWithAttr = graph.nodes[node]
        nodesWithAttr.append((node, nodeWithAttr))

    for edge in edges:
        edgeWithAttr = graph.get_edge_data(edge[0], edge[1])
        edgesWithAttr.append((edge[0], edge[1], edgeWithAttr['weight']))

    result = {
        'nodes': nodesWithAttr,
        'edges': edgesWithAttr
    }

    return result


def getAllEdges(graph, partitionMembers):
    edgepairs = []
    for node in partitionMembers:
        nodeName = node[0]
        neighbors = graph[nodeName]
        for neighbor, weight in neighbors.items():
            if graph.nodes[neighbor]['partition'] == partitionMembers[0][1]['partition']:
                edgepairs.append((nodeName, neighbor, weight['weight']))
    return edgepairs


def find_connecting_edges(graph, borderNodes):
    connectingEdges = []
    outsideNode = []
    for node in borderNodes:
        nodeName = node[0]
        neighbors = graph[nodeName]
        for neighbor, weight in neighbors.items():
            # if not equal -> different partition (connecting edge)
            if node[1]['partition'] != graph.nodes[neighbor]['partition']:
                outsideNode.append((neighbor, graph.nodes[neighbor]))
                connectingEdges.append((nodeName, neighbor, weight['weight']))
    return connectingEdges, outsideNode


def create_extended_component(graph, partitionMembers, partitionName, transitNetwork):
    # find border nodes
    # note to myself, I've already found them in create_IDT maybe give border and nonboder nodes as a argument
    # to create_IDT and create_extended_component
    borderNodes = []
    for member in partitionMembers:
        if member[1]['borderNode'] == True:
            borderNodes.append(member)

    shortestPathList = []
    # take each border node and compute shortest path to other border nodes
    for sourceBorderNode in borderNodes:
        for destinationBorderNode in borderNodes:
            if sourceBorderNode != destinationBorderNode:
                shortestPath = nx.dijkstra_path(graph, sourceBorderNode[0], destinationBorderNode[0], weight='weight')
                shortestPathList.append((sourceBorderNode[0], destinationBorderNode[0], shortestPath))

    # shortestPathList only got node names we need to get full node and edge information
    shortestPathNodesAndEdges = get_all_nodes_in_path(graph, shortestPathList)
    # print(shortestPathNodesAndEdges)

    # create a new graph as a extendedComponent
    extendedComponentGraph = nx.Graph()
    extendedComponentGraph.add_nodes_from(shortestPathNodesAndEdges['nodes'])
    extendedComponentGraph.add_weighted_edges_from(shortestPathNodesAndEdges['edges'])

    transitNetwork.add_nodes_from(shortestPathNodesAndEdges['nodes'])
    transitNetwork.add_weighted_edges_from(shortestPathNodesAndEdges['edges'])

    # find connecting edges for component
    connecting_edges, outsideNodes = find_connecting_edges(graph, borderNodes)
    transitNetwork.add_nodes_from(outsideNodes)
    transitNetwork.add_weighted_edges_from(connecting_edges)

    # till now we added shortest path edges and nodes in extended component
    # also we need to add all paths and edges in this component

    # for nodes we can directly add partitionMembers into the extendedComponentGraph data structure is suitable
    extendedComponentGraph.add_nodes_from(partitionMembers)

    # for edges find all edge pair in this component
    allEdgePairs = getAllEdges(graph, partitionMembers)
    extendedComponentGraph.add_weighted_edges_from(allEdgePairs)

    return extendedComponentGraph

    # for elem in shortestPathNodesAndEdges:
    # node larin bi listesini koycan
    # G.add_nodes_from([
    #     (4, {"color": "red"}),
    #     (5, {"color": "green"}),
    # ])

    # su sekilde de edgleri koyucan (2, 3, {'weight': 3.1415})


def calculate_b2b_distance(transitNetwork, partition1, partition2):
    partition1BorderNodes = []
    partition2BorderNodes = []
    # c1 deki her border node'u al
    # border nodes of Cn
    for node in transitNetwork.nodes(data=True):
        if node[1]['borderNode'] == True and node[1]['partition'] == partition1:
            partition1BorderNodes.append(node[0])
        elif node[1]['borderNode'] == True and node[1]['partition'] == partition2:
            partition2BorderNodes.append(node[0])
        else:
            # check for other nodes
            continue

    # c1 deki her border node icin c2 ye dijkstra kosup path distance i alicaz.
    source_target_distance = []
    for node_source in partition1BorderNodes:
        for node_target in partition2BorderNodes:
            pathLength = nx.dijkstra_path_length(transitNetwork, node_source, node_target, weight='weight')
            source_target_distance.append((node_source, node_target, pathLength))

    return source_target_distance



def create_CDM(transitNetwork, partitions):
    partitionNumber = len(partitions)
    cdm_matrix = [[-1 for _ in range(partitionNumber)] for _ in range(partitionNumber)]

    for index1, partition1 in enumerate(partitions):
        for index2, partition2 in enumerate(partitions):
            if partition1 == partition2:
                continue
            else:
                distanceList = calculate_b2b_distance(transitNetwork, partition1, partition2)
                cdm_matrix[index1][index2] = distanceList

    return cdm_matrix


def preprocess(*, graph, transitNetwork, partitions):
    # preprocess will return one transit network and C* (extended component) for each component
    extendedComponents = {}  # icine name: component name extendedcomponentgraph : graph i seklinde yapalim

    # for each component
    for partition in partitions:
        # first create IDT
        partitionMembers = [(node, attrs) for node, attrs in graph.nodes(data=True) if attrs["partition"] == partition]
        create_IDT(graph=graph, partitionMembers=partitionMembers)

        # second create extended component
        ex = create_extended_component(graph, partitionMembers, partition, transitNetwork)
        extendedComponents[partition] = ex

    # print(extendedComponents['C1'].nodes)
    # print(extendedComponents['C1'].edges)
    #
    # print(extendedComponents['C2'].nodes)
    # print(extendedComponents['C2'].edges)
    #
    # print("transit network...")
    # print(transitNetwork.nodes(data=True))
    # print(transitNetwork.edges(data=True))

    cdm_maxtix = create_CDM(transitNetwork, partitions)
    return {'extendedComponentList': extendedComponents,
            'cdm_matrix': cdm_maxtix}
