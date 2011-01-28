// This file is part of the Wyone automated theorem prover.
//
// Wyone is free software; you can redistribute it and/or modify 
// it under the terms of the GNU General Public License as published 
// by the Free Software Foundation; either version 3 of the License, 
// or (at your option) any later version.
//
// Wyone is distributed in the hope that it will be useful, but 
// WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
// the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public 
// License along with Wyone. If not, see <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce. 

package wyone.core;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import wyil.jvm.rt.BigRational;
import wyil.lang.Type;
import wyil.lang.Value;

/**
 * <p>
 * A Wyone value is something fixed, which cannot be further subdivided. For
 * example, an integer or real number. Values are important as, for each
 * variable in a formula, we must attempt to find an appropriate value for it.
 * This must satisfy the variables type and, in addition, enable the formula to
 * be reduced to true. If it is impossible to do this, then the formula is
 * <i>unsatisfiable</i>; or, if we run out of time trying then it's
 * satisfiability is <i>unknown</i>.
 * <p>
 * 
 * @author djp
 * 
 */
public class WValue<T extends Value> implements WExpr {
	protected T value; 

	public WValue(T value) {
		this.value = value;
	}
	
	public Type type(SolverState state) {
		return value.type();
	}
	
	public List<WExpr> subterms() {
		return Collections.EMPTY_LIST;
	}
	
	public boolean equals(Object o) {
		if(o instanceof WValue) {
			WValue v = (WValue) o;
			return value.equals(v.value);
		}
		return false;
	}
	
	public int hashCode() {
		return value.hashCode();
	}
	
	public int compareTo(WExpr e) {
		if(e instanceof WValue) {
			WValue v = (WValue) e;
			throw new RuntimeException("Need to implement WValue.compareTo()");
		} else if(e.cid() < CID) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * Substituting into a value has no effect. However, we need this method
	 * because it overrides Expr.substitute.
	 */
	public WValue substitute(Map<WExpr,WExpr> binding) {
		return this;
	}

	private static class Constraint extends WValue<Value.Bool> implements WConstraint {
		public Constraint(Value.Bool b) {
			super(b);
		}
		
		public Constraint substitute(Map<WExpr,WExpr> binding) {
			return this;
		}
	}
	
	public static class Number extends WValue<Value.Real> {
		public Number(BigRational r) {
			super(Value.V_REAL(r));
		}
		public Number(BigInteger r) {
			super(Value.V_REAL(BigRational.valueOf(r)));
		}
		
		public BigInteger numerator() {
			return value.value.numerator();
		}
		
		public BigInteger denominator() {
			return value.value.denominator();
		}
		
		public Number add(Number n) {
			return new Number(value.value.add(n.value.value));
		}
		
		public Number subtract(Number n) {
			return new Number(value.value.subtract(n.value.value));
		}
		
		public Number multiply(Number n) {
			return new Number(value.value.multiply(n.value.value));
		}
		
		public Number divide(Number n) {
			return new Number(value.value.divide(n.value.value));
		}
		
		public Number negate() {
			return new Number(value.value.negate());
		}
	}
	
	// ====================================================================
	// CONSTANTS
	// ====================================================================
	
	public final static Constraint FALSE = new Constraint(Value.V_BOOL(false));
	public final static Constraint TRUE = new Constraint(Value.V_BOOL(true));
	
	public final static WValue ZERO = new WValue(Value.V_REAL(BigRational.ZERO));
	public final static WValue ONE = new WValue(Value.V_REAL(BigRational.ONE));
	public final static WValue MONE = new WValue(Value.V_REAL(BigRational.MONE));
	
	// ====================================================================
	// CID
	// ====================================================================
	
	public int cid() {
		return CID;
	}
	
	private static final int CID = WExprs.registerCID();
}
