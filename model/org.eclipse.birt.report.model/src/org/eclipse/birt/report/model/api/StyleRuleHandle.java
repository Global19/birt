/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.util.OperatorUtil;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Represents the handle of style rule. This abstract class provides the common
 * methods for <code>HighlightRuleHandle</code> and <code>MapRuleHandle</code>.
 * The style rule provides the operator, value1, and value2 to compute boolean
 * result.
 */

public abstract class StyleRuleHandle extends StructureHandle
{

	/**
	 * Constructs the handle of style rule.
	 * 
	 * @param valueHandle
	 *            the value handle for style rule list of one property
	 * @param index
	 *            the position of this style rule in the list
	 */

	public StyleRuleHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the operator. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @return the operator
	 */

	public String getOperator( )
	{
		return getStringProperty( StyleRule.OPERATOR_MEMBER );
	}

	/**
	 * Sets the operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>MAP_OPERATOR_EQ
	 * <li>MAP_OPERATOR_NE
	 * <li>MAP_OPERATOR_LT
	 * <li>MAP_OPERATOR_LE
	 * <li>MAP_OPERATOR_GE
	 * <li>MAP_OPERATOR_GT
	 * <li>MAP_OPERATOR_BETWEEN
	 * <li>MAP_OPERATOR_NOT_BETWEEN
	 * <li>MAP_OPERATOR_NULL
	 * <li>MAP_OPERATOR_NOT_NULL
	 * <li>MAP_OPERATOR_TRUE
	 * <li>MAP_OPERATOR_FALSE
	 * <li>MAP_OPERATOR_LIKE
	 * <li>MAP_OPERATOR_ANY
	 * </ul>
	 * 
	 * @param operator
	 *            the operator to set
	 * @throws SemanticException
	 *             if operator is not in the choice list.
	 */

	public void setOperator( String operator ) throws SemanticException
	{
		ActivityStack stack = getModule( ).getActivityStack( );
		stack.startTrans( CommandLabelFactory.getCommandLabel(
				MessageConstants.CHANGE_PROPERTY_MESSAGE,
				new String[]{StyleRule.OPERATOR_MEMBER} ) );
		try
		{
			setProperty( StyleRule.OPERATOR_MEMBER, operator );
			int level = OperatorUtil.computeStyleRuleOperatorLevel( operator );
			switch ( level )
			{
				case OperatorUtil.OPERATOR_LEVEL_ONE :
					setValue2( null );
					break;
				case OperatorUtil.OPERATOR_LEVEL_TWO :
					break;
				case OperatorUtil.OPERATOR_LEVEL_ZERO :
					setValue2( null );
					setValue1( (List) null );
					break;
				case OperatorUtil.OPERATOR_LEVEL_NOT_EXIST :
					break;
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

	}

	/**
	 * Returns the value 1.
	 * 
	 * @return the value 1
	 */

	public String getValue1( )
	{
		return getCompatibleValue1( StyleRule.VALUE1_MEMBER, getValue1List( ) );
	}

	/**
	 * Gets the value1 expression list. For most map operator, there is only one
	 * expression in the returned list. However, map operator 'in' may contain
	 * more than one expression.
	 * 
	 * @return the value1 expression list.
	 */

	public List getValue1List( )
	{
		List valueList = (List) getProperty( StyleRule.VALUE1_MEMBER );
		if ( valueList == null || valueList.isEmpty( ) )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( valueList );
	}

	/**
	 * Sets the value 1.
	 * 
	 * @param value1
	 *            the value 1 to set
	 */

	public void setValue1( String value1 )
	{
		setPropertySilently( StyleRule.VALUE1_MEMBER, value1 );
	}

	/**
	 * Sets the value 1 expression list.
	 * 
	 * @param value1List
	 *            the value 1 expression list to set
	 * @throws SemanticException
	 *             if the instance in the list is not valid
	 */

	public void setValue1( List value1List ) throws SemanticException
	{
		setProperty( StyleRule.VALUE1_MEMBER, value1List );
	}

	/**
	 * Returns the value 2.
	 * 
	 * @return the value 2
	 */

	public String getValue2( )
	{
		return getStringProperty( StyleRule.VALUE2_MEMBER );
	}

	/**
	 * Sets the value 2.
	 * 
	 * @param value2
	 *            the value 2 to set
	 */

	public void setValue2( String value2 )
	{
		setPropertySilently( StyleRule.VALUE2_MEMBER, value2 );
	}
}