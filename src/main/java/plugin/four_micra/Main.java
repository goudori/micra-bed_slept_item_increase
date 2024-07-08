package plugin.four_micra;

import java.util.Arrays;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private int count;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  //　プレイヤーがマイクラサーバーに参加した時のイベント
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
//    ベッドアイテム一個追加
    ItemStack bedItem = new ItemStack(Material.YELLOW_BED, 1);
    player.getInventory().addItem(bedItem);
  }


  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    if (count % 2 == 0) {
      // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
      Firework firework = world.spawn(player.getLocation(), Firework.class);

      // 花火オブジェクトが持つメタ情報を取得。
      FireworkMeta fireworkMeta = firework.getFireworkMeta();

      // メタ情報に対して設定を追加したり、値の上書きを行う。
      // 今回は青色で星型の花火を打ち上げる。
      fireworkMeta.addEffect(
          FireworkEffect.builder()
              .withColor(Color.BLUE)
              .with(Type.STAR)
              .withFlicker()
              .build());
      fireworkMeta.setPower(0);

      // 追加した情報で再設定する。
      firework.setFireworkMeta(fireworkMeta);
    }
    count++;
  }


  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
// Stream API　→繰り返し処理を短く書く方法
    //  マイクラベッドで寝た時に最大所持数64個まで増やすチート方法
    final ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        .filter(
            item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64
        )
        .forEach(item -> item.setAmount(64));
    player.getInventory().setContents(itemStacks);
  }
}
