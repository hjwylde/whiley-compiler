// This file is part of the Whiley-to-Java Compiler (wyjc).
//
// The Whiley-to-Java Compiler is free software; you can redistribute 
// it and/or modify it under the terms of the GNU General Public 
// License as published by the Free Software Foundation; either 
// version 3 of the License, or (at your option) any later version.
//
// The Whiley-to-Java Compiler is distributed in the hope that it 
// will be useful, but WITHOUT ANY WARRANTY; without even the 
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
// PURPOSE.  See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public 
// License along with the Whiley-to-Java Compiler. If not, see 
// <http://www.gnu.org/licenses/>
//
// Copyright 2010, David James Pearce. 

package wyjc.ast.exprs.real;

import java.util.*;

import wyjc.ModuleLoader;
import wyjc.ast.attrs.Attribute;
import wyjc.ast.exprs.*;
import wyjc.ast.types.*;
import wyjc.util.Pair;
import wyjc.util.ResolveError;
import wyjc.util.Triple;
import wyone.core.*;
import wyone.theory.logic.*;
import wyone.theory.numeric.*;
import static wyone.theory.logic.WFormulas.*;
import static wyone.theory.numeric.WNumerics.*;


public class RealNegate extends UnOp<Expr> implements Expr {		
	public RealNegate(Expr e, Attribute... attributes) {
		super(e,Types.T_REAL(null),attributes);		
	}
	
	public RealNegate(Expr e, Collection<Attribute> attributes) {
		super(e,Types.T_REAL(null),attributes);		
	}
	
    public Expr substitute(Map<String,Expr> binding) {
		Expr e = expr.substitute(binding);		
		return new RealNegate(e,attributes());
	}
    
    public Expr replace(Map<Expr,Expr> binding) {
		Expr t = binding.get(this);
		if(t != null) {
			return t;
		} else {
			Expr l = expr.replace(binding);			
			return new RealNegate(l,attributes());
		}
	}
	    
    public Expr reduce(Map<String, Type> environment) {
		Expr l = expr.reduce(environment);

		if (l instanceof RealVal) {
			RealVal i1 = (RealVal) l;			
			return new RealVal(i1.value().negate(),attributes());
		} else {
			// no further reduction possible.
			return new RealNegate(l, attributes());
		}
	}
    
    public Pair<WExpr,WFormula> convert(Map<String, Type> environment, ModuleLoader loader) throws ResolveError {		
		Pair<WExpr,WFormula> p = expr.convert(environment, loader);			
		return new Pair<WExpr,WFormula>(negate(p.first()), p.second());
	}
	
    public String toString() {
    	return "-" + expr.toString();
    }
}
