"use client";

import { useRef, useEffect } from "react";
import { createChart, CandlestickData, IChartApi, ISeriesApi, CandlestickSeries, LineSeries} from "lightweight-charts";

type Candle = {
    timestamp: string,
    open: number,
    high: number,
    low: number,
    close: number
};

type IndicatorPoint = {
    timestamp: string,
    value: number
};

type Indicators = {
    vwap?: IndicatorPoint[]
};

type Props = {
    candles: Candle[],
    height: number,
    // indicators?: {
    //     vwap?: {timestamp: string, value: number}[];
    // };
};

export default function CandleChart({candles, height = 400}: Props){
    const containerRef = useRef<HTMLDivElement | null>(null);

    const chartRef = useRef<IChartApi | null>(null);
    const candleSeriesRef = useRef<ISeriesApi<"Candlestick"> | null>(null);
    useEffect(() => {
        if (!containerRef.current)
            return;

        const chart = createChart(containerRef.current, {
            height: height,
            width: containerRef.current.clientWidth
        });

        const candlestickSeries = chart.addSeries(CandlestickSeries, {
              upColor: "#26a69a",
              downColor: "#ef5350",
              borderVisible: false,
              wickUpColor: "#26a69a",
              wickDownColor: "#ef5350",
        });

        chartRef.current = chart;
        candleSeriesRef.current = candlestickSeries;

        return () => {
            chart.remove();
            chartRef.current = null;
            candleSeriesRef.current = null;
        }
        }, [])

    useEffect(() =>{

        if (!candleSeriesRef.current || !candles?.length )
            return;

        const formattedData = candles.map(c => ({
            time: Math.floor(new Date(c.timestamp).getTime() / 1000),
            open: c.open,
            high: c.high,
            low: c.low,
            close: c.close
        }));

        candleSeriesRef.current.setData(formattedData);

        requestAnimationFrame(() => {
            chartRef.current?.priceScale('right').applyOptions({
                autoScale: true,
            });
        });

        
    }, [candles]);

    return <div ref={containerRef} style={{height: "400px"}}/>;

}
