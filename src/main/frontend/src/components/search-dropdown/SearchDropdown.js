import React, { Component } from 'react';
import { DebounceInput } from 'react-debounce-input';
import { connect } from 'react-redux';
import styled from 'styled-components';

const numberOfCharacters = 4;
const Results = styled.div`
  background: white;
  height: 150px;
  overflow: auto;
  display: ${props =>
    props.dropdownIsActive || props.inputIsActive ? 'block' : 'none'};
`;

class SearchDropdown extends Component {
  state = {
    inputIsActive: false,
    dropdownIsActive: false
  };

  mouseEnter() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    } else {
      this.setState({
        dropdownIsActive: true
      });
    }
  }

  mouseLeave() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    } else {
      this.setState({
        dropdownIsActive: false,
        inputIsActive: true
      });
      this.inputReference.focus();
    }
  }

  mouseOver() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    }
  }

  checkIfInputIsValid() {
    if (this.inputReference.value.length >= numberOfCharacters) {
      this.setState({
        inputIsActive: true
      });
    } else {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    }
  }

  search() {
    this.checkIfInputIsValid();
    this.props.dispatch(this.props.searchAction(this.inputReference.value));
  }

  render() {
    return (
      <div className="position-relative">
        <DebounceInput
          inputRef={ref => {
            this.inputReference = ref;
          }}
          onFocus={() => this.checkIfInputIsValid()}
          onBlur={() =>
            this.setState({
              inputIsActive: false
            })
          }
          minLength={numberOfCharacters}
          debounceTimeout={300}
          onChange={e => this.search(e)}
          placeholder="Users search (type 4 characters to search)"
          className="w-100 border"
        />
        <Results
          onMouseEnter={() => this.mouseEnter()}
          onMouseLeave={() => this.mouseLeave()}
          onMouseOver={() => this.mouseOver()}
          inputIsActive={this.state.inputIsActive}
          dropdownIsActive={this.state.dropdownIsActive}
          className="results position-absolute w-100 p-4 border border-top-0"
        >
          {this.props.children(this.inputReference)}
        </Results>
      </div>
    );
  }
}

export default connect()(SearchDropdown);
