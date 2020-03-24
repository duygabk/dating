import React, { Component } from 'react';
import { Input } from 'antd';

import './SearchBox.css';

const { Search } = Input;

class SearchBox extends Component {
    render() {
        return (
            <div className= "box-search">
                <Search placeholder="input search text" onSearch={value => console.log(value)} enterButton />
            </div>

        )
    }
}

export default SearchBox;