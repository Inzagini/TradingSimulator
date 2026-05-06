"use client";

import { useRef, useEffect } from "react";
import { createChart, CandlestickData, CandlestickSeries, LineSeries} from "lightweight-charts";

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
    indicators?: {
        vwap?: {timestamp: string, value: number}[];
    };
};

export default function CandleChart({candles, indicators}: Props){
    const chartContainerRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        if (!chartContainerRef.current)
            return;

        const chart = createChart(chartContainerRef.current, {
            width: chartContainerRef.current.clientWidth,
            height: 400,
            timeScale: {
                timeVisible: true,
                secondsVisible: true
            }
        });

        const candlestickSeries = chart.addSeries(CandlestickSeries, {
              upColor: "#26a69a",
              downColor: "#ef5350",
              borderVisible: false,
              wickUpColor: "#26a69a",
              wickDownColor: "#ef5350",
        });

        const vwapSeries = chart.addSeries( LineSeries,{
            color: "blue",
            lineWidth: 2
        })


        const formattedData: CandlestickData[] = candles.map(c => ({
            time: Math.floor(new Date(c.timestamp).getTime() / 1000),
            open: c.open,
            high: c.high,
            low: c.low,
            close: c.close
        }));

        candlestickSeries.setData(formattedData);


        chart.timeScale().fitContent();

        return () => chart.remove();
        
    }, [candles]);

    return <div ref={chartContainerRef} style={{height: "400px"}}/>;

}
