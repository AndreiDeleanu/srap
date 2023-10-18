<template>
	<div class="bx--tile">
		<div class="title bx--type-gamma">{{title}}</div>
		<div class="chart" ref="chart">
			<svg ref="svg"></svg>
			<div class="text">
				<p class="percentage bx--type-beta" ref="percentage"></p>
			</div>
		</div>
		<div class="info bx--type-gamma">{{value}} of {{total}}</div>
	</div>
</template>

<script>
    const d3 = require('d3');

    export default {
        name: "CircleGauge",
        mounted: function () {
            const tau = 2 * Math.PI;
            const radius = Math.floor(this.$refs.chart.clientWidth / 4);
            const padding = 20;
            const boxSize = (radius + padding) * 2;
            const amount = this.value;
            const total = this.total;
            const ratio = amount / total;

            const arc = d3.arc()
                .innerRadius(radius)
                .outerRadius(radius - 10)
                .startAngle(0);

            const svg = d3.select(this.$refs.svg)
                .attr('width', boxSize)
                .attr('height', boxSize);

            const g = svg
                .append('g')
                .attr('transform', `translate(${boxSize / 2}, ${boxSize / 2})`);

            // Background Arc
            const background = g.append('path')
                .datum({endAngle: tau})
                .style('fill', '#dfe3e6')
                .attr('d', arc);

            // Foreground Arc
            const foreground = g.append('path')
                .datum({endAngle: 0})
                .style('fill', '#00a68f')
                .transition()
                .duration(1000)
                .attrTween('d', arcTween(ratio * tau));

            // Text Labels
            const percentageText = d3.select(this.$refs.percentage);
            percentageText
                .style('opacity', 0)
                .transition()
                .duration(1000)
                .style('opacity', 1)
                .text(`${amount}%`);

            // Animation function
            function arcTween(newAngle) {
                return function (d) {
                    const interpolate = d3.interpolate(d.endAngle, newAngle);
                    return function (t) {
                        d.endAngle = interpolate(t);
                        return arc(d);
                    }
                }
            }
        },
        props: {
            title: {
                type: String,
                required: true
            },
            value: {
                type: Number,
                required: true
            },
            total: {
                type: Number,
                required: true
            }
        }
    }
</script>

<style lang="scss" scoped>
	.bx--tile {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		.title, .info {
			font-weight: 400;
		}
		.chart {
			position: relative;
			.text {
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%, -50%);
				text-align: right;
				.percentage {
					opacity: 0;
					margin: 0;
					font-weight: 400;
				}
			}
		}
	}
</style>