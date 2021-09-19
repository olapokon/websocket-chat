/**
 * @param e {HTMLElement} the HTML element that was mutated
 * @param m {MutationRecord} the mutation record
 */
function autoScroll(e, m) {
    if (m.addedNodes.length < 1)
        return;
    const addedElementHeight = m.addedNodes[0].parentElement.lastElementChild.clientHeight;
    const overflowHeight = e.scrollHeight - e.clientHeight;
    const isAtBottom = e.scrollHeight <= e.clientHeight
        || e.scrollTop >= e.scrollHeight - addedElementHeight - e.clientHeight;
    if (!isAtBottom)
        return;
    e.scrollTop = overflowHeight;
}

/**
 * Automatically scrolls down to the bottom of an HTML element, when the element is mutated,
 * unless it is not already scrolled all the way down.
 *
 * @param e {HTMLElement} the HTML element to autoscroll
 */
export default function onAppend(e) {
    const observer = new MutationObserver((mutations) =>
        mutations.forEach(m => autoScroll(e, m)))
    observer.observe(e, {
        subtree: true,
        childList: true,
    })
}
