import React from "react";
import {Editor, EditorState} from "draft-js";
import {stateToHTML} from 'draft-js-export-html';
import $ from "jquery";
import "draft-js/dist/Draft.css";
import "../../assets/style/text-editor.css"
import AddSong from "../../views/AddSong/AddSong";
import {getNoteValueMapSortedByHashesAndFlats} from "../../util/MusicUtils";

const SongEditor = React.forwardRef((props, ref) => {
    const noteValueMap = getNoteValueMapSortedByHashesAndFlats();

    const [editorState, setEditorState] = React.useState(() =>
        EditorState.createEmpty()
    );

    const editor = React.useRef(null);

    // as the second argument
    React.useImperativeHandle(ref, () => ({
        toHtml() {
            const html = stateToHTML(editorState.getCurrentContent());
            const $html = $(`<div id="parent">${html}</div>`, {html: html});
            $html.find("p").each((index, p) => {
                if (index % 2 === 0) {
                    p.className = "chords";

                    let innerText = p.innerText;
                    Object.entries(noteValueMap)
                        .forEach(noteValue => {
                            const htmlSpan = `<span class="chord chord-${noteValue[1]}"></span>`;
                            innerText = innerText.replaceAll(noteValue[0], htmlSpan)
                        })
                    p.innerHTML = innerText;
                } else {
                    p.className = "lyrics";
                }
            })

            return $html.html();
        }
    }));

    const onChange = (newEditorState) => {
        setEditorState(newEditorState);
    }

    const setClassName = (contentBlock) => {
        const index = editorState.getCurrentContent().getBlocksAsArray().findIndex(c => c === contentBlock);
        if (index % 2 === 0) {
            return "chords";
        }

        return "lyrics";
    }

    return (
        <div
            style={{
                border: "1px solid #CFCFCF",
                minHeight: "800px",
                cursor: "text",
                padding: "15px 33px",
                fontSize: "22px",
                fontFamily: "Segoe UI",
                fontWeight: 300
            }}
        >
            <Editor
                ref={editor}
                editorState={editorState}
                onChange={onChange}
                blockStyleFn={setClassName}
                placeholder="Write something!"
            />
        </div>
    );
})

export default SongEditor;