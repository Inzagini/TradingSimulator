export async function getCandles(symbol: string){
    const res = await fetch(`http://localhost:8000/candles?symbol=${symbol}`);

    if (!res.ok)
        throw new Error("Failed to fetch candles")

    return res.json();
}
