package com.badoo.ribs.core.plugin

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

interface Plugin

interface NodeAware : Plugin {
    fun init(node: Node<*>) {}
}

interface NodeLifecycleAware : Plugin {
    fun onAttach(nodeLifecycle: Lifecycle) {}

    fun onDetach() {}
}

interface ViewAware<V : RibView> : Plugin {
    fun onViewCreated(view: V, viewLifecycle: Lifecycle) {}
}

interface ViewLifecycleAware : Plugin {
    fun onAttachToView(parentViewGroup: ViewGroup) {}

    fun onDetachFromView(parentViewGroup: ViewGroup) {}
}

interface SubtreeChangeAware : Plugin {
    fun onAttachChildNode(child: Node<*>) {}

    fun onDetachChildNode(child: Node<*>) {}
}

interface SubtreeViewChangeAware : Plugin {
    fun onAttachChildView(child: Node<*>) {}

    fun onDetachChildView(child: Node<*>) {}
}

interface AndroidLifecycleAware : Plugin {
    fun onStart() {}

    fun onStop() {}

    fun onResume() {}

    fun onPause() {}
}

interface SavesInstanceState : Plugin {
    fun onSaveInstanceState(outState: Bundle) {}
}

interface SystemAware : Plugin {
    fun onLowMemory() {}
}

/**
 * A plugin that reacts to back presses. Return true to signal back press has been handled.
 *
 * It's advised you have one instance of this / Node, otherwise back press handling order will depend
 * on the order of your plugins. Rather, have one class implement it, and add your dispatching
 * logic there if you have more complex scenario.
 */
interface BackPressHandler : Plugin {

    fun handleBackPress(): Boolean =
        false
}

/**
 * SubtreeBackPressHandler is used only for purposes of routing mechanisms.
 *
 * It will be asked twice by the framework: first, before everything else, and last, after everything else.
 *
 * In everyday cases you probably don't want to implement this interface. If you do, make sure
 * you understand it before use.
 *
 * @see BackPressHandler for most common case.
 */
interface SubtreeBackPressHandler : Plugin {
    /**
     * Dispatched first by Node, even before asking child Nodes in the subtree.
     *
     * Routing can use this to pop overlays as top priority.
     *
     * Important:
     * In almost any other case you probably DON'T want to react on this level, otherwise
     * your business logic on the highest levels will take precedence before your active leaf
     * nodes. In a typical setup this means your application quits before on-screen nodes
     * even have a chance to react.
     */
    fun handleBackPressFirst(): Boolean =
        false

    /**
     * This level is dispatched last by Node if all above priority levels failed to handle
     * a back press.
     *
     * Routing can use this to e.g. pop content back stack.
     */
    fun handleBackPressFallback(): Boolean =
        false
}
