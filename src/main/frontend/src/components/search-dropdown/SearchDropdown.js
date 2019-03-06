import React, { Component } from 'react';
import { DebounceInput } from 'react-debounce-input';
import { connect } from 'react-redux';
import styled from 'styled-components';

const numberOfCharacters = 4;
const Results = styled.div`
  background: white;
  height: 150px;
  overflow: auto;
`;

class SearchDropdown extends Component {
  state = {
    inputIsActive: false,
    dropdownIsActive: false
  };

  mouseEnter = () => {
    this.setState({
      dropdownIsActive: true
    });
  };

  mouseLeave = () => {
    this.setState({
      dropdownIsActive: false,
      inputIsActive: true
    });
    this.inputReference.focus();
  };

  inputBlur = () => {
    this.setState({
      inputIsActive: false
    });
  };

  search = () => {
    this.setState({
      inputIsActive: true
    });

    if (this.inputReference.value.length >= numberOfCharacters) {
      this.props.dispatch(this.props.searchAction(this.inputReference.value));
    }
  };

  render() {
    const shouldRenderResults =
      this.inputReference &&
      this.inputReference.value.length >= 4 &&
      (this.state.inputIsActive || this.state.dropdownIsActive);

    return (
      <div className="position-relative">
        <DebounceInput
          inputRef={ref => {
            this.inputReference = ref;
          }}
          onBlur={this.inputBlur}
          minLength={numberOfCharacters}
          debounceTimeout={300}
          onChange={this.search}
          placeholder="Users search (type 4 characters to search)"
          className="w-100 border"
        />
        {shouldRenderResults && (
          <Results
            onMouseEnter={this.mouseEnter}
            onMouseLeave={this.mouseLeave}
            className="results position-absolute w-100 p-4 border border-top-0"
          >
            {this.props.children(this.inputReference)}
          </Results>
        )}
      </div>
    );
  }
}

export default connect()(SearchDropdown);
