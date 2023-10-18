<template>
    <div :id="id"/>
</template>

<script>
import "jodit/build/jodit.min.css";
import Jodit from "jodit";
export default {
    name: "Jodit",
    props: {
        value: { type: String, required: true },
        id: { type: String, default: "editor" },
        buttons: {
            type: Array,
            default: () => [
                "paragraph",
                "|",
                "bold",
                "italic",
                "|",
                "ul",
                "link"
            ]
        },
        extraButtons: { type: Array, default: null },
        config: {
            type: Object,
            default: () => {
                return {
                    autofocus: true,
                    allowResizeY: false,
                    toolbarSticky: false,
                    showWordsCounter: false,
                    showXPathInStatusbar: false,
                    height: 250,
                    limitChars: 750
                };
            }
        }
    },
    computed: {
        editorConfig() {
            const config = { ...this.config };
            if (this.buttons) {
                config.buttons = this.buttons;
                config.buttonsMD = this.buttons;
                config.buttonsSM = this.buttons;
                config.buttonsXS = this.buttons;
            }
            if (this.extraButtons) config.extraButtons = this.extraButtons;
            return config;
        }
    },
    data() {
        return {
            editor: null
        };
    },
    watch: {
        value(val) {
            // this.editor.value = val;
            if (val != null) {
                if (this.editor.value !== val) this.editor.value = val;
            } else {
                this.editor.value = "<p></p>";
            }
        }
    },
    mounted() {
        this.editor = new Jodit(`#${this.id}`, this.editorConfig);
        if (this.value != null) {
            this.editor.value = this.value;
        } else {
            this.editor.value = "<p></p>";
        }
        this.editor.events.on("change", newValue => {
            this.$emit("input", newValue);
        });
    },
    beforeDestroy() {
        this.editor.destruct();
    }
};
</script>
