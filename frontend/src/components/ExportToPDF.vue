<template>
    <button @click="exportToPdf()" class="bx--btn bx--btn--secondary bx--btn--sm" type="button">
        Export to PDF
        <svg
            class="bx--btn__icon"
            width="16"
            height="16"
            viewBox="0 0 16 16"
            xmlns="http://www.w3.org/2000/svg"
        >
            <svg width="12" height="16" viewBox="0 0 12 16">
                <path
                    d="M10.581 3.996L8.014 1.429l.026 2.559 2.541.008zM7.011 1H1v14h10V4.997l-2.963-.01a1 1 0 0 1-.997-.99L7.01 1zM1 0h7l4 4v11a1 1 0 0 1-1 1H1a1 1 0 0 1-1-1V1a1 1 0 0 1 1-1z"
                ></path>
                <path d="M3 9h6v1H3zm0 3h6v1H3z"></path>
            </svg>
        </svg>
    </button>
</template>

<script>
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import { mapState } from "vuex";

export default {
    name: "ExportToPDF",
    computed: {
        ...mapState({
            container: state => state.pdf.container
        })
    },
    data() {
        return {
            filename: "SRAP_export"
        };
    },
    methods: {
        updateStyles() {
            return new Promise(resolve => {
                resolve();
            });
        },
        getImage(elements, index, offset, pdf, fn, filename, dispatch) {
            const pdfWidth = 210; //mm
            const pdfHeight = 297; // mm
            const pdfMargins = {
                top: 10,
                right: 10,
                bottom: 10,
                left: 10
            };
            const pdfAreaWidth = pdfWidth - pdfMargins.right - pdfMargins.left;
            const pdfAreaHeight =
                pdfHeight - pdfMargins.top - pdfMargins.bottom;

            const e = elements[index];
            e.scrollIntoView();
            return html2canvas(e, { scale: 1, logging: false }).then(canvas => {
                const image = [
                    canvas.toDataURL("image/png"),
                    canvas.width,
                    canvas.height
                ];
                const aspectRatio = image[1] / image[2];
                var newOffset = isNaN(aspectRatio)
                    ? offset
                    : offset + pdfAreaWidth / aspectRatio;
                if (newOffset > pdfAreaHeight) {
                    pdf.insertPage();
                    newOffset = pdfMargins.top;
                }
                if (image[1] != 0) {
                    pdf.addImage(
                        image[0],
                        "PNG",
                        pdfMargins.left,
                        offset,
                        pdfAreaWidth,
                        pdfAreaWidth / aspectRatio
                    );
                }
                if (index < elements.length - 1) {
                    fn(
                        elements,
                        index + 1,
                        newOffset,
                        pdf,
                        fn,
                        filename,
                        dispatch
                    );
                } else {
                    pdf.save(filename);
                    dispatch("spinner/loading", false);
                }
            });
        },
        exportToPdf: function() {
            let pdf = new jsPDF();
            const container = document.querySelector("#pdf-content");
            let content = container.querySelectorAll(".bx--row");
            if (content.length <= 0) {
                content = container.querySelectorAll("tr");
            }
            this.$store.dispatch("spinner/loading", true);
            this.updateStyles().then(() =>
                this.getImage(
                    content,
                    0,
                    10,
                    pdf,
                    this.getImage,
                    this.filename,
                    this.$store.dispatch
                )
            );
        }
    }
};
</script>

<style scoped>
</style>
