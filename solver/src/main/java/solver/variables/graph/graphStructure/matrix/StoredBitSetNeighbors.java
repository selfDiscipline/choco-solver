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

package solver.variables.graph.graphStructure.matrix;

import choco.kernel.memory.IEnvironment;
import choco.kernel.memory.structure.S64BitSet;
import solver.variables.graph.INeighbors;
import solver.variables.graph.graphStructure.iterators.AbstractNeighborsIterator;

/**
 * Created by IntelliJ IDEA.
 * User: chameau
 * Date: 9 f�vr. 2011
 */
public class StoredBitSetNeighbors extends S64BitSet implements INeighbors {

    public StoredBitSetNeighbors(IEnvironment environment, int nbits) {
        super(environment, nbits);
    }

    @Override
    public void add(int element) {
        this.set(element,true);
    }

    @Override
    public boolean remove(int element) {
        boolean isIn = this.get(element);
        if (isIn) {
            this.set(element, false);
        }
        return isIn;
    }

    @Override
    public boolean contain(int element) {
        return this.get(element);
    }

    @Override
    public int neighborhoodSize() {
        return this.cardinality();
    }

    public int nextValue(int from) {
        return this.nextSetBit(from);
    }

    @Override
    public AbstractNeighborsIterator<StoredBitSetNeighbors> iterator() {
        return new SBIterator(this);  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
	public int getFirstElement() {
		return nextSetBit(0);
	}
    
    private class SBIterator extends AbstractNeighborsIterator<StoredBitSetNeighbors>{

    	private int index;
    	
		public SBIterator(StoredBitSetNeighbors data) {
			super(data);
			index = -1;
		}

		@Override
		public boolean hasNext() {
			return data.nextSetBit(index+1)>=0;
		}

		@Override
		public int next() {
			index = data.nextSetBit(index+1);
			return index;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    }
}
