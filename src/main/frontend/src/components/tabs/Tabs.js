import React, { Component, Children } from 'react';
import styled from 'styled-components';

const TabsWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
`;

const TabButton = styled.button`
  flex: 1;
  height: 50px;
  padding: 0px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  background: transparent;
  outline: none;
  transition: border-color 0.2s ease-in;
  border: none;
  border-bottom: 4px solid ${props => (props.selected ? 'red' : '#eee')};
  &:focus,
  &:active {
    border-bottom: 4px solid ${props => (props.selected ? 'red' : '#dc3545')};
  }
`;

const TabList = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
`;

const Content = styled.div`
  flex: 1;
  width: 100%;
  padding-top: 16px;
`;

class Tabs extends Component {
  state = {
    selectedTab: 0
  };

  selectTab = tabIndex => {
    this.setState({ selectedTab: tabIndex });
  };

  render() {
    const { children } = this.props;
    const { selectedTab } = this.state;

    return (
      <TabsWrapper>
        <TabList>
          {Children.map(children, ({ props: { label } }, index) => (
            <TabButton
              selected={selectedTab === index}
              onClick={() => this.selectTab(index)}
            >
              {label}
            </TabButton>
          ))}
        </TabList>

        <Content>
          {Children.map(
            children,
            (comp, index) => (selectedTab === index ? comp : undefined)
          )}
        </Content>
      </TabsWrapper>
    );
  }
}

export default Tabs;
