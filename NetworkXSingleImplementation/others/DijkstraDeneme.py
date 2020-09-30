import networkx as nx

elist = [
    ("a", "b", 5),
    ("a", "c", 7),
    ("b", "d", 2),
    ("c", "d", 3),
    ("d", "e", 1),
    ("c", "e", 2)
]

G = nx.Graph()
G.add_weighted_edges_from(elist)

for edge in G.edges(data=True):
    print(edge)

dList = nx.dijkstra_path(G, "a", "e", weight='weight')
for item in dList:
    print(item)

dictA = G.get_edge_data("a", "b")

