import networkx as nx
from networkx import jit_data, jit_graph
import json

G = None

# with open("testlist") as source:
#     loaded = json.loads(source)
#     G = jit_graph(loaded)

file = open("data.txt")
data = json.load(file)
print(data)
G = jit_graph(data)

print(G.number_of_nodes())
nodeList = list(G.nodes(data=True))
print(nodeList)
