import networkx as nx
from networkx import jit_data
import json
import pprint

def createParDISTExampleGraph():
    G = nx.Graph()

    # Partition C1 Nodes
    G.add_node("n0", partition="C1", borderNode=False)
    G.add_node("n1", partition="C1", borderNode=False)
    G.add_node("n2", partition="C1", borderNode=True)
    G.add_node("n3", partition="C1", borderNode=True)
    G.add_node("n4", partition="C1", borderNode=True)
    G.add_node("n5", partition="C1", borderNode=False)

    # Partition C2 Nodes
    G.add_node("n6", partition="C2", borderNode=True)
    G.add_node("n7", partition="C2", borderNode=True)
    G.add_node("n9", partition="C2", borderNode=False)
    G.add_node("n11", partition="C2", borderNode=False)
    G.add_node("n12", partition="C2", borderNode=True)

    # Partition C3 Nodes
    G.add_node("n14", partition="C3", borderNode=False)
    G.add_node("n15", partition="C3", borderNode=False)
    G.add_node("n16", partition="C3", borderNode=True)
    G.add_node("n19", partition="C3", borderNode=True)
    G.add_node("n20", partition="C3", borderNode=False)

    # Partition C4 Nodes
    G.add_node("n8", partition="C4", borderNode=True)
    G.add_node("n10", partition="C4", borderNode=False)
    G.add_node("n13", partition="C4", borderNode=False)
    G.add_node("n17", partition="C4", borderNode=False)
    G.add_node("n18", partition="C4", borderNode=True)

    # Edges with Weights
    G.add_edge("n0", "n1", weight=4)
    G.add_edge("n0", "n4", weight=4)
    G.add_edge("n0", "n5", weight=4)
    G.add_edge("n1", "n2", weight=2)
    G.add_edge("n1", "n3", weight=8)
    G.add_edge("n3", "n2", weight=7)
    G.add_edge("n2", "n5", weight=3)
    G.add_edge("n5", "n4", weight=3)

    G.add_edge("n3", "n7", weight=2)
    G.add_edge("n2", "n6", weight=2)
    G.add_edge("n4", "n8", weight=3)

    G.add_edge("n7", "n6", weight=2)
    G.add_edge("n7", "n12", weight=5)
    G.add_edge("n6", "n9", weight=3)
    G.add_edge("n9", "n11", weight=3)
    G.add_edge("n11", "n12", weight=2)

    G.add_edge("n12", "n16", weight=4)

    G.add_edge("n16", "n15", weight=2)
    G.add_edge("n16", "n20", weight=5)
    G.add_edge("n15", "n14", weight=2)
    G.add_edge("n15", "n19", weight=5)
    G.add_edge("n15", "n20", weight=5)

    G.add_edge("n19", "n18", weight=2)

    G.add_edge("n18", "n17", weight=3)
    G.add_edge("n18", "n10", weight=7)
    G.add_edge("n18", "n13", weight=6)
    G.add_edge("n17", "n13", weight=4)
    G.add_edge("n13", "n10", weight=4)
    G.add_edge("n13", "n8", weight=5)
    G.add_edge("n10", "n8", weight=3)

    return G

def writeGraphasJSON(graph, *, prettyPrint=False):
    # convert to JIT JSON
    graph_as_json = jit_data(graph)

    if (prettyPrint):
        writeGraphasJSONwithoutPretty(graph_as_json, indent=4)
    else:
        writeGraphasJSONwithoutPretty(graph_as_json, indent=None)

# uses pprint but adds single quote
def writeGraphasJSONwithPretty(source):
    # with open("parDIST_example.json", "wt") as output:
    #     parsed = json.loads(source)
    pp = pprint.PrettyPrinter(stream=open("others/parDIST_example.json", "w"))
    parsed = json.loads(source)
    pp.pprint(parsed)

def writeGraphasJSONwithoutPretty(source, indent):
    with open("others/parDIST_example.json", "w") as output:
        parsed = json.loads(source)
        json.dump(parsed, output, indent=indent)

if __name__ == "__main__":
    G = createParDISTExampleGraph()
    node_n2 = G.nodes['n2']
    print(node_n2)
    #writeGraphasJSON(G, prettyPrint=True)

    edge1 = G["n5"]
    print(edge1)




