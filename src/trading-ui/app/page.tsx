import { getCandles } from "@/lib/api/candles";
import CandleChart from "@/component/chart/CandleChart";

export default async function Home() {
  const data = await getCandles("ES=F");

  return (
    <main>
      <h1>Trading Simulator</h1>

      <CandleChart candles={data.data} height={400} />
      <CandleChart candles={data.data} height={150} />
      <CandleChart candles={data.data} height={100} />
    </main>
  );

}
