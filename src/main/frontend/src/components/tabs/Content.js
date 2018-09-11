import React from 'react';
import styled from 'styled-components';

const TabContent = styled.div`
  flex: 1;
  width: 100%;
  padding-top: 16px;
`;

const Content = ({ content }) => <TabContent>{content}</TabContent>;

export default Content;
