const webpack = require('webpack');
const path = require('path');
const MiniCssExtractPlugin=require("mini-css-extract-plugin");

module.exports = {
    entry: './ui/entry.js',
    output: {path: path.resolve(__dirname, 'public/compiled'), filename: 'bundle.js'},
    module: {
        rules: [{
            test: /\.jsx?$/,
            exclude: /node_modules/,
            include: /ui/,
            loader: 'babel-loader',
            query: {
                presets: ['es2015', 'react']
            }
        },{
            test: /\.scss$/,
            use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader']
        }]
    },
    plugins: [
        new MiniCssExtractPlugin({ filename: "styles.css" })
    ]    
}