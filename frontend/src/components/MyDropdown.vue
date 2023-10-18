<template>
	<div>
		<div class="label-wrapper">
			<label :class="{'bx--label--disabled': disabled}" :for="uuid" class="bx--label" v-if="hasLabel">{{labelText}}</label>
			<div class="bx--tooltip--icon" v-if="hasIcon">
				<button :aria-label="labelIconText" class="bx--tooltip__trigger bx--tooltip--icon__bottom">
					<svg height="16" viewBox="0 0 16 16" width="16" xmlns="http://www.w3.org/2000/svg">
						<g fill-rule="evenodd">
							<path
								d="M8 14.5a6.5 6.5 0 1 0 0-13 6.5 6.5 0 0 0 0 13zM8 16A8 8 0 1 1 8 0a8 8 0 0 1 0 16z"
								fill-rule="nonzero"></path>
							<path d="M9 13H7V7h2z" fill-rule="nonzero"></path>
							<circle cx="8" cy="4" r="1"></circle>
						</g>
					</svg>
				</button>
			</div>
		</div>
		<ul :class="{'disabled': disabled}" :id="uuid" class="bx--dropdown" data-dropdown data-value tabindex="0">
			<li class="bx--dropdown-text">
				{{defaultText}}
			</li>
			<svg class="bx--dropdown__arrow" fill-rule="evenodd" height="5" viewBox="0 0 10 5" width="10">
				<path d="M10 0L5 5 0 0z"></path>
			</svg>
			<li>
				<ul class="bx--dropdown-list">
					<li :data-value="item" :key="item" class="bx--dropdown-item" data-option
						v-for="(item, index) in items">
						<a :id="'link-item-' + index" class="bx--dropdown-link" href="javascript:void(0)" tabindex="-1">{{item}}</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</template>

<script>
    import {Dropdown} from 'carbon-components';
    import helpers from '../helpers/helpers';

    export default {
        name: "MyDropdown",
        props: {
            defaultText: {
                type: String,
                required: true
            },
            items: {
                type: Array,
                required: true
            },
            selectedIndex: {
                type: Number
            },
            labelText: {
                type: String
            },
            labelIconText: {
                type: String
            },
            disabled: {
                type: Boolean
            }
        },
        data() {
            return {
                uuid: "data-dropdown-" + helpers.getRandomUuid(),
                instance: null
            }
        },
        mounted: function () {
            const dropdown = this.$el.querySelector("#" + this.uuid);
            this.instance = Dropdown.create(dropdown);
            const _this = this;
            if (this.selectedIndex !== undefined && this.selectedIndex !== null) {
                this.instance.select(this.$el.querySelector("#link-item-" + this.selectedIndex));
            } else {
                // this.deselect();
            }
            dropdown.addEventListener('dropdown-selected', function (e) {
                _this.$emit('itemSelected', e.detail.item.text);
            });
            if (this.disabled) {
                this.instance.release();
            }
        },
        computed: {
            hasLabel: function () {
                return this.labelText && this.labelText.trim().length > 0
            },
            hasIcon: function () {
                return this.labelIconText && this.labelIconText.trim().length > 0
            }
        },
        methods: {
            // for future reference, do not remove
            // deselect() {
            //     [...this.$el.querySelectorAll(".bx--dropdown--selected")].forEach(item => {
            //         item.classList.remove("bx--dropdown--selected");
            //         this.$el.querySelector("li.bx--dropdown-text").innerHTML = this.defaultText;
            //     });
            // }
        },
        destroyed: function () {
            if (this.instance) {
                this.instance.release();
            }
        }
    }
</script>

<style lang="scss" scoped>
	.label-wrapper {
		display: flex;
		align-items: start;

		.bx--tooltip--icon {
			margin-left: 8px;
		}
	}

	.disabled {
		cursor: not-allowed !important;
		/*pointer-events: none !important;*/
	}
</style>