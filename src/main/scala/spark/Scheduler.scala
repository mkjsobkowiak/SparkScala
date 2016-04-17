package spark

import java.util.logging
import java.util.logging.Level

import akka.actor.ActorSystem
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext

import scala.collection.mutable
import scala.concurrent.duration._

class Scheduler(val lines: mutable.Queue[RDD[Int]], val streamingContext: StreamingContext) {
  private[this] val actorSystem = ActorSystem()
  private[this] val scheduler = actorSystem.scheduler
  private[this] implicit val executor = actorSystem.dispatcher
  private[this] val randomizer = scala.util.Random

  initScheduler

  def initScheduler = {
    scheduler.schedule(
      initialDelay = DurationInt(4).seconds,
      interval = DurationInt(1).seconds,
      runnable = task
    )
  }
  
  def task: Runnable = {
    new Runnable {
      def run() {
        val randomList = List.range(1, 1000).map(x => randomizer.nextInt(200))
        lines += streamingContext.sparkContext.makeRDD(randomList)
      }
    }
  }
}
