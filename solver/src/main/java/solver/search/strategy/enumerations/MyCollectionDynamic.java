/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package solver.search.strategy.enumerations;

import choco.kernel.memory.IEnvironment;
import choco.kernel.memory.IStateInt;
import solver.search.strategy.enumerations.sorters.AbstractSorter;
import solver.search.strategy.enumerations.validators.IValid;

import java.util.LinkedList;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 15/12/10
 */
public class MyCollectionDynamic<A> extends MyCollection<A> {

    IStateInt from;
    final int to;

    LinkedList<AbstractSorter<A>> cs;

    IValid<A> vs;

    A current;

    public MyCollectionDynamic(A[] c, LinkedList<AbstractSorter<A>> cs, IValid<A> vs, IEnvironment env) {
        super(c);
        this.cs = cs;
        this.vs = vs;
        this.from = env.makeInt();
        this.to = c.length;
    }

    void minima() {
        filter();
        // only the first element
        int _from = from.get();
        int to = this.to - 1;
        for(int c = 0; c < cs.size() && to > _from; c++){
            to = cs.get(c).minima(elements, _from, to);
        }
    }

    void filter() {
        int _from = from.get();
        for (int i = _from; i < to; i++) {
            if (!vs.valid(elements[i])) {
                A tmp = elements[i];
                elements[i] = elements[_from];
                elements[_from] = tmp;
                _from++;

            }
        }
        from.set(_from);
    }

    public boolean hasNext() {
        minima(); // minima call filter, filter may change from !
        boolean isNotEmpty = (from.get() < elements.length);
        if (isNotEmpty) {
            current = elements[from.get()];
        }
        return isNotEmpty;
    }

    public A next() {
        return current;
    }
}
