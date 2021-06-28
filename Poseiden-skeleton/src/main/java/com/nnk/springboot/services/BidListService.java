/**
 * 
 */
package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.BidList;

/**
 * @author jerome
 *
 */

public interface BidListService{
	public List<BidList> findAll();
	public void save(BidList bidList);
	public void deleteById(Integer id);
	public Optional<BidList> findById(Integer id);
	public Boolean existsById(Integer id);
	
}
