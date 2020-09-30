import networkx as nx
from networkx import jit_data
import json

G = nx.Graph()

G.add_node("6", color="red")
G.add_nodes_from([
    ("4", {"color": "red"}),
    ("5", {"color": "green"}),
])

G.add_edge("6", "4")
G.add_edge("6", "5")


print(G.number_of_nodes())
nodeList = list(G.nodes(data=True))
print(nodeList)

print(G.number_of_edges())
edgeList = list(G.edges)
print(edgeList)

result = jit_data(G)
parsed = json.loads(result)

with open("data.txt", "w") as outfile:
    json.dump(parsed, outfile)

#nx.write_adjlist(G, "testlist")

