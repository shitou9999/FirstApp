/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package cn.com.bjnews.thinker.view;

/**
 * 
 * This listner is useful to make changes before/after refreshing.
 *
 */
public interface IRefreshListner {

	
	
	/**
	 * This function will invoke just before refreshing starts.
	 */
	public void preRefresh();

	
	public void interruptPreRefresh();
	
	public void Refresh();
	/**
	 * This function will invoke just before refreshing stops.
	 */
	public void postRefresh(); 
}