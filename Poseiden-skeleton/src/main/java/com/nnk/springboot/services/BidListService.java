/**
 * 
 */
package com.nnk.springboot.services;

import java.util.List;

import com.nnk.springboot.domain.BidList;

/**
 * @author jerome
 *
 */

public interface BidListService {
	
	public List<BidList> findAll();
}
