import React, { Component } from 'react';

export default class Radio extends Component {
  render() {
    return (
      <div className="container">
        <div className="radio">
          <input id="radio-1" name="radio" type="radio" />
          <label htmlFor="radio-1" className="radio-label">
            Checked
          </label>
        </div>

        <div className="radio">
          <input id="radio-2" name="radio" type="radio" />
          <label htmlFor="radio-2" className="radio-label">
            Unchecked
          </label>
        </div>

        <div className="radio">
          <input id="radio-3" name="radio" type="radio" />
          <label htmlFor="radio-3" className="radio-label">
            Disabled
          </label>
        </div>
      </div>
    );
  }
}
