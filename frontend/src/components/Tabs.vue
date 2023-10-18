<template>
    <div class="js--tabs">
        <nav>
            <span
                v-for="s in sections"
                :key="s"
                :class="{'selected': selected === s}"
                href="javascript:void(0)"
                @click="toggleSection(s)"
            >{{s}}</span>
        </nav>
        <div
            v-for="s in sections"
            :key="'section-' + s"
            class="section"
            :class="{'visible': selected === s}"
        >
            <slot :name="'slot-'+s"/>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Tabs',
    props: {
        sections: {
            type: Array,
            default: () => []
        },
    },
    data() {
        return {
            selected: ''
        }
    },
    mounted() {
        this.selected = this.sections[0]
    },
    methods: {
        toggleSection(section) {
            this.selected = section
        }
    }
}
</script>

<style lang="scss" scoped>
    .js--tabs {
        nav {
            line-height: 2rem;
            margin-bottom: 2rem;

            span {
                text-decoration: none;
                color: #152935;
                padding: 0.75rem 0;
                cursor: pointer;
                font-weight: 600;
                font-size: 0.85rem;
                transition: color 300ms;

                &.selected {
                    color: #3d70b2;
                    border-bottom: 2px solid #3d70b2;
                }

                &:hover {
                    color: #3d70b2;
                }

                &:not(:first-of-type) {
                    margin-left: 3rem;
                }
            }
        }

        .section {
            display: none;

            &.visible {
                display: block;
            }
        }
    }
</style>

