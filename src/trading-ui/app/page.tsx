import { getCandles } from "@/lib/api/candles";
import CandleChart from "@/component/chart/CandleChart";

export default async function Home() {
  const data = await getCandles("ES=F");

  return (
    <main className="h-screen p-4 bg-black text-white">
      <div className="grid h-full grid-cols-[1fr_4fr_1fr] gap-1">
        
        {/* LEFT */}
        <div className="grid grid-rows-3 gap-1">
          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>

          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>

          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>
        </div>

        {/* Middle */}
        <div className="grid grid-rows-[1fr_3fr] gap-1">
          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>

          <div className="grid grid-cols-2 gap-1" >

            <div className="rounded-xl overflow-hidden bg-zinc-900">
               <CandleChart candles={data.data} />
            </div>

            <div className="rounded-xl overflow-hidden bg-zinc-900">
               <CandleChart candles={data.data} />
            </div>

          </div>
        </div>

        {/* Right */}

        <div className="grid grid-rows-2 gap-1">
          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>

          <div className="rounded-xl overflow-hidden bg-zinc-900">
            <CandleChart candles={data.data} />
          </div>
        </div>

      </div>
    </main>);

}
