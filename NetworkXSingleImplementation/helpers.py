import networkx as nx


def get_partition_names(*, graph):
    partitionNames = {value for node, value in graph.nodes.data("partition")}
    return partitionNames