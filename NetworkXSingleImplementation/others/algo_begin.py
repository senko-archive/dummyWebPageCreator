from NetworkXSingleImplementation.ParDISTExampleGraphGenerator import createParDISTExampleGraph
from NetworkXSingleImplementation.IDT import IDT
import networkx as nx

def getEdgeCouples(shortestPath):
    firstIndex = 0
    secondIndex = 2
    listLength = len(shortestPath)
    listOfEdgeCouples = []
    while secondIndex <= listLength:
        print("shortest path nodes -> ", shortestPath[firstIndex:secondIndex])
        listOfEdgeCouples.append((shortestPath[firstIndex:secondIndex]))
        firstIndex = firstIndex + 1
        secondIndex = secondIndex + 1
    return listOfEdgeCouples

def getEdge(graph, edgeCouples):
        edge = graph.get_edge_data(edgeCouples[0], edgeCouples[1])
        node1 = graph.nodes[edgeCouples[0]]
        node2 = graph.nodes[edgeCouples[1]]
        print(f"shortest path edge info -> {node1} {node2} {edge}")
        return [(edgeCouples[0], node1), (edgeCouples[1], node2)]

def createExtendedComponent(*, graph, componentName):
    partitionMembers = [(node, attrs) for node, attrs in graph.nodes(data=True) if attrs["partition"] == componentName]
    print("partitionMembers -> ", partitionMembers)

    # partitoinMembers icinden border node'lari bul
    borderNodes = []
    for member in partitionMembers:
        if member[1]["borderNode"] == True:
            borderNodes.append(member)

    print("border nodes -> ", borderNodes)

    shortestPathList = []
    # her border node u source olarak al ve diger boder node'lara target yap
    for sourceBorderNode in borderNodes:
        for destinationBorderNode in borderNodes:
            if sourceBorderNode != destinationBorderNode:
                shortestPathResult = nx.dijkstra_path(graph, sourceBorderNode[0], destinationBorderNode[0], weight='weight')
                shortestPathList.append((sourceBorderNode[0], destinationBorderNode[0], shortestPathResult))

    #print(shortestPathList)
    # simdilik undirected graph oldugu icin x in y ye shortest pathini aldiktan sonra
    # y nin x e almana gerenk yok

    # bu shortest pathler sana node lari vericek list icinde
    # ordan tek tek edgeleri alip node ve edgeleri ayri bir subgraph a at simdilik
    for shortestPath in shortestPathList:
        listOfEdgeCouples = getEdgeCouples(shortestPath[2])

        nodes_and_edges_shortest = []
        for edgeCouples in listOfEdgeCouples:
            nodes_and_edge_with_attr = getEdge(graph, edgeCouples)
            nodes_and_edges_shortest.append(nodes_and_edge_with_attr)

        print(nodes_and_edges_shortest)
        exdendedList = []
        for innerList in nodes_and_edges_shortest:
            exdendedList.append(innerList[0])
            exdendedList.append(innerList[1])

        print("exdendedList ---> ", exdendedList)

    # yeni bi graph yarat ve buraya C1 partitiondaki tum node ve edgeleri bi at
    extendedComponent = nx.Graph()
    extendedComponent.add_nodes_from(partitionMembers)
    extendedComponent.add_nodes_from(exdendedList)
    for node in extendedComponent.nodes(data=True):
        print(node)

    # edgeleri de eklememiz lazim
    for node in partitionMembers:
        nodeName = node[0]
        neighbors = graph[nodeName]
        for neighbor, weight in neighbors.items():
            print(nodeName, neighbor, weight)
            extendedComponent.add_edge(nodeName, neighbor, weight=weight['weight'])

    for edge in extendedComponent.edges(data=True):
        print(edge)

def getPartitionNames(*, graph):
    partitionNames = {value for node, value in G.nodes.data("partition")}
    return partitionNames

def createIDT(graph, partitionName):
    # get all nodes belong that partition
    partitionMembers = [(node, attrs) for node, attrs in graph.nodes(data=True) if attrs["partition"] == partitionName]
    print(partitionMembers)
    # get nodes exlcude border nodes
    nonBorderNodes = []
    borderNodes = []
    for member in partitionMembers:
        if member[1]['borderNode'] == False:
            nonBorderNodes.append(member)
        else:
            borderNodes.append(member)

    # add empty IDT for border nodes
    for borderNode in borderNodes:
        idt = IDT(borderNode[0])
        graph.nodes[borderNode[0]]['IDT'] = idt

    #print("non border nodes are: ", nonBorderNodes)
    #print("border nodes are: ", borderNodes)

    # for each non border node run djikstra to border nodes
    for nonBorderNode in nonBorderNodes:
        # nonBorderNode[0] -> node name, borderNode[0] -> node name
        idt = IDT(nonBorderNode[0])
        for borderNode in borderNodes:
            distance = nx.dijkstra_path_length(graph, nonBorderNode[0], borderNode[0], weight='weight')
            path = nx.dijkstra_path(graph, nonBorderNode[0], borderNode[0])
            distanceAndPathDict = {'borderNode': borderNode[0], 'distance': distance, 'path': path}
            idt.idtList.append(distanceAndPathDict)
        # add idt to node
        graph.nodes[nonBorderNode[0]]['IDT'] = idt

if __name__ == "__main__":
    G = createParDISTExampleGraph()

    partitionNames = getPartitionNames(graph=G)

    # create IDT (Internal Distance Table) for each partition
    for partition in partitionNames:
        createIDT(G, partition)

    exampleIDT = G.nodes["n7"]['IDT']
    print(exampleIDT.idtList)




    #for partition in partitionNames:
    #createExtendedComponent(graph=G, componentName="C1")





