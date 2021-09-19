/**
 * @param e {HTMLElement} the HTML element that was mutated
 * @param addedNodes {NodeList} the nodes that have been added to the element
 */
function autoScroll(e, addedNodes) {
    console.log(addedNodes);
    const addedElementHeight = Array.from(addedNodes)
        .reduce((height, node) => height + node.parentElement.lastElementChild.clientHeight, 0);
    const overflowHeight = e.scrollHeight - e.clientHeight;
    const isAtBottom = e.scrollHeight <= e.clientHeight
        || e.scrollTop >= e.scrollHeight - addedElementHeight - e.clientHeight;
    if (!isAtBottom)
        return;
    e.scrollTop = overflowHeight;
}

/**
 * Automatically scrolls down to the bottom of an HTML element, when {@link Node}s are added to the element,
 * unless it is not already scrolled all the way down.
 *
 * @param e {HTMLElement} the HTML element to autoscroll
 */
export default function onAddNode(e) {
    const observer = new MutationObserver((mutations) =>
        mutations.forEach(m => {
            if (!m.addedNodes || m.addedNodes.length < 1)
                return;
            autoScroll(e, m.addedNodes);
        }));
    observer.observe(e, {
        subtree: true,
        childList: true,
    });
}
