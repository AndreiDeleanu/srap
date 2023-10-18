<template>
    <div class="bx--tile">
        <div class="pie-chart">
            <h2 class="bx--graph-header">{{ title }}</h2>
            <div class="pie-chart-body">
                <div class="graph-container">
                    <svg ref="svg" :class="'pie-cahrt-'+chartId"></svg>
                    <div v-if="sum == 0" class="notice">No data to display</div>
                    <div ref="tooltip" :class="'tooltip'+chartId">
                        <p :class="'amount'+chartId">
                            <span ref="amount"/>
                            <span ref="percentage" class="percentage"/>
                        </p>
                        <p ref="item" :class="'item'+chartId"/>
                    </div>
                </div>
                <div class="pie-chart-legend">
                    <p v-for="(d, index) in data" :key="d[0]">
                        <span class="color" :style="'background: '+color[index]"/>
                        {{ d[0] }}
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import * as d3 from "d3";

export default {
    name: "PieChart",
    props: {
        data: {
            type: Array,
            default: () => [
                ["Gryffindor", 23],
                ["Slytherin", 12],
                ["Ravenclaw", 19],
                ["Hufflepuff", 15],
                ["Teachers", 5]
            ]
        },
        title: {
            type: String,
            default: ""
        },
        color: {
            type: Array,
            default: () => [
                "#8A8F9A",
                "#8CD211",
                "#F7D322",
                "#E71D32",
                "#56D2BB"
            ]
            // ['#3b1a40', '#473793', '#F7D322', '#00a68f', '#56D2BB']
        }
    },
    data: () => {
        return {
            radius: 96
        };
    },
    computed: {
        chartId() {
            return +Math.floor(Math.random() * 100000 + 1);
        },
        size: function() {
            return this.radius * 2;
        },
        sum() {
            const sum = this.data.reduce((result, row) => {
                return result + row[1];
            }, 0);
            return sum;
        }
    },
    watch: {
        data(data) {
            this.init();
        }
    },
    mounted() {
        this.init();
    },
    methods: {
        init: function() {
            const { tooltipRef, amount, item, percentage } = this.$refs;
            const sum = this.data.reduce((result, row) => {
                return result + row[1];
            }, 0);
            const svg = d3
                .select(this.$refs.svg)
                .attr("width", this.size)
                .attr("height", this.size)
                .append("g")
                .attr("class", "group-container")
                .attr(
                    "transform",
                    `translate(${this.size / 2}, ${this.size / 2})`
                );

            const color = d3.scaleOrdinal(this.color);

            const pie = d3
                .pie()
                .sort(null)
                .value(d => d[1]);

            const path = d3
                .arc()
                .outerRadius(this.radius - 10)
                .innerRadius(this.radius - 40);

            const pathTwo = d3
                .arc()
                .outerRadius(this.radius)
                .innerRadius(this.radius - 40);

            const arc = svg
                .selectAll(".arc" + this.chartId)
                .data(pie(this.data))
                .enter()
                .append("g")
                .attr("class", "arc");

            arc.append("path")
                .attr("d", path)
                .attr("fill", (d, i) => color(i))
                .attr("stroke-width", 2)
                .attr("stroke", "#FFFFFF")
                .on("mouseover", function(d) {
                    d3.select(this)
                        .transition()
                        .style("cursor", "pointer")
                        .attr("d", pathTwo);

                    // eslint-disable-next-line no-unused-vars
                    const tooltip = d3
                        .select(tooltipRef)
                        .style("display", "inherit");

                    const amountt = d3.select(amount);
                    const itemm = d3.select(item);
                    const percentagee = d3.select(percentage);

                    const percent = Math.round((d.data[1] / sum) * 100);

                    amountt.text(`${d.data[1]}`);

                    percentagee.text("(" + percent + "%)");

                    itemm.text(`${d.data[0]}`);
                })
                .on("mouseout", function(d) {
                    // eslint-disable-next-line no-unused-vars
                    const tooltip = d3
                        .select(tooltipRef)
                        .style("display", "none");

                    d3.select(this)
                        .transition()
                        .attr("d", path);
                });
        }
    }
};
</script>

<style lang="scss" scoped>
.pie-chart {
    .pie-chart-body {
        display: flex;
        flex-flow: row nowrap;

        .graph-container {
            display: flex;
            flex-direction: column;
            flex-grow: 2;
            align-items: center;
            justify-content: center;

            .notice {
                position: absolute;
            }

            div[class^="tooltip"] {
                position: absolute;
                top: 50%;
                display: flex;
                flex-direction: column;
                align-items: center;
                text-shadow: -1px -1px 0 #fff, 1px -1px 0 #fff, -1px 1px 0 #fff,
                    1px 1px 0 #fff;
                transform: translateY(-25%);

                p[class^="amount"] {
                    font-size: 1.8125rem;
                    font-weight: 300;
                    color: #152935;

                    .percentage {
                        font-size: 0.9rem;
                    }
                }

                p[class^="item"] {
                    font-size: 0.875rem;
                    font-weight: 400;
                    color: #5a6872;
                }
            }
        }

        .pie-chart-legend {
            display: flex;
            flex-flow: column nowrap;
            flex-grow: 1;
            justify-content: center;

            .color {
                display: inline-block;
                width: 10px;
                height: 10px;
                margin-right: 5px;
            }
        }
    }
}
</style>
