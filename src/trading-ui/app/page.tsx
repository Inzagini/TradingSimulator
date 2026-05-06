import { getCandles } from "@/lib/api/candles";
import CandleChart from "@/component/chart/CandleChart";

export default async function Home() {
  const data = await getCandles("ES");


  console.log(`Data ${data.data}`)
  return (
    <main>
      <h1>Trading Simulator</h1>
      <CandleChart candles={data.data}/>
    </main>
  );

}
