/*
 * My first class with over 500 lines
 */
package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import net.server.Server;
import tools.DatabaseConnection;

/**
 *
 * @author F4keName
 */
public class AuctionSystemHandler {

    public static String getEquipAuctions() {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT id, itemid FROM auctions WHERE type = 1");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    x += "#L" + rs.getInt("id") + "##v" + rs.getInt("itemid") + "##l";
                }
                rs.close();
                ps.close();
                if ("".equals(x)) {
                    x += "#L0#There are no equip auctions.#l";
                }
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getEquipAuctions(): " + e);
            }
            return "";
        }
    }

    public static String getOwnedEquipAuctions(MapleClient c) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT id, itemid FROM auctions WHERE type = 1 AND cid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    x += "#L" + rs.getInt("id") + "##v" + rs.getInt("itemid") + "##l";
                }
                rs.close();
                ps.close();
                if ("".equals(x)) {
                    x += "#L0#You have no equip auctions.#l";
                }
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getOwnedEquipAuctions(): " + e);
            }
            return "";
        }
    }

    public static String getEquipInfo(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                x += ("#bAuction ID - #r" + id + "\r\n");
                while (rs.next()) {
                    x += ("#bItem - #r#v" + rs.getInt("itemid") + "#\r\n");
                    if (rs.getInt("str") > 0) {
                        x += ("#bSTR - #r" + rs.getInt("str") + "\r\n");
                    }
                    if (rs.getInt("dex") > 0) {
                        x += ("#bDEX - #r" + rs.getInt("dex") + "\r\n");
                    }
                    if (rs.getInt("int") > 0) {
                        x += ("#bINT - #r" + rs.getInt("int") + "\r\n");
                    }
                    if (rs.getInt("luk") > 0) {
                        x += ("#bLUK - #r" + rs.getInt("luk") + "\r\n");
                    }
                    if (rs.getInt("hp") > 0) {
                        x += ("#bHP - #r" + rs.getInt("hp") + "\r\n");
                    }
                    if (rs.getInt("mp") > 0) {
                        x += ("#bMP - #r" + rs.getInt("mp") + "\r\n");
                    }
                    if (rs.getInt("watk") > 0) {
                        x += ("#bWATK - #r" + rs.getInt("watk") + "\r\n");
                    }
                    if (rs.getInt("matk") > 0) {
                        x += ("#bMATK - #r" + rs.getInt("matk") + "\r\n");
                    }
                    if (rs.getInt("wdef") > 0) {
                        x += ("#bWDEF - #r" + rs.getInt("wdef") + "\r\n");
                    }
                    if (rs.getInt("mdef") > 0) {
                        x += ("#bMDEF - #r" + rs.getInt("mdef") + "\r\n");
                    }
                    if (rs.getInt("acc") > 0) {
                        x += ("#bACC - #r" + rs.getInt("acc") + "\r\n");
                    }
                    if (rs.getInt("avoid") > 0) {
                        x += ("#bAVOID - #r" + rs.getInt("avoid") + "\r\n");
                    }
                    if (rs.getInt("hands") > 0) {
                        x += ("#bHANDS - #r" + rs.getInt("hands") + "\r\n");
                    }
                    if (rs.getInt("speed") > 0) {
                        x += ("#bSPEED - #r" + rs.getInt("speed") + "\r\n");
                    }
                    if (rs.getInt("jump") > 0) {
                        x += ("#bJUMP - #r" + rs.getInt("jump") + "\r\n");
                    }
                    if (rs.getInt("locked") > 0) {
                        x += ("#bLOCKED - #r" + "Yes" + "\r\n");
                    }
                    if (rs.getInt("vicious") > 0) {
                        x += ("#bVICIOUS USED - #r" + rs.getInt("vicious") + "\r\n");
                    }
                    if (rs.getInt("itemlevel") > 1) {
                        x += ("#bITEM LEVEL - #r" + rs.getInt("itemlevel") + "\r\n");
                    }
                    if (rs.getInt("itemexp") > 0) {
                        x += ("#bITEM EXP - #r" + rs.getInt("itemexp") + "\r\n");
                    }
                    x += ("#bSLOTS LEFT - #r" + rs.getInt("upgradeslots") + "\r\n");
                    if (rs.getInt("line1") > 0) {
                        x += ("#bPotential 1 - #r" + showPoT(rs.getInt("line1")) + "\r\n");
                    }
                    if (rs.getInt("line2") > 0) {
                        x += ("#bPotential 2 - #r" + showPoT(rs.getInt("line2")) + "\r\n");
                    }
                    if (rs.getInt("line3") > 0) {
                        x += ("#bPotential 3 - #r" + showPoT(rs.getInt("line3")) + "\r\n");
                    }
                    x += ("#bSeller - #r" + getNameById(rs.getInt("cid")) + "\r\n#dPress ok to know the price of this item.");
                }
                ps.close();
                rs.close();
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getEquipInfo(): " + e);
            }
            return "";
        }
    }

    public static String getOwnedItemAuctions(MapleClient c) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT id, itemid FROM auctions WHERE type = 2 AND cid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    x += "#L" + rs.getInt("id") + "##v" + rs.getInt("itemid") + "##l";
                }
                ps.close();
                rs.close();
                if ("".equals(x)) {
                    x += "#L0#You have no item auctions#l";
                }
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getOwnedItemAuctions(): " + e);
            }
            return "";
        }
    }

    public static String getItemAuctions() {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT id, itemid FROM auctions WHERE type = 2");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    x += ("#L" + rs.getInt("id") + "##v" + rs.getInt("itemid") + "##l");
                }
                ps.close();
                rs.close();
                if ("".equals(x)) {
                    x += "#L0#There are no item auctions.#l";
                }
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getItemAuctions(): " + e);
            }
            return "";
        }
    }

    public static String getItemInfo(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT cid, itemid, quantity FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                x += ("#bAuction ID - #r" + id + "\r\n");
                while (rs.next()) {
                    x += ("#bItem - #r#v" + rs.getInt("itemid") + "#\r\n");
                    x += ("#bQuantity - #r" + rs.getInt("Quantity") + "\r\n");
                    x += ("#bSeller - #r" + getNameById(rs.getInt("cid")) + "\r\n#dPress ok to know the price of this item.");
                }
                rs.close();
                ps.close();
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getItemInfo(): " + e);
            }
            return "";
        }
    }

    public static String getPrice(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT ice, black, tear, cloud, meso, quantity, vp, shards, cid FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("shards") > 0) {
                        x += ("#bCrystal Shards - #r" + rs.getInt("shards") + "\r\n");
                    }
                    if (rs.getInt("ice") > 0) {
                        x += ("#bIce Pieces - #r" + rs.getInt("ice") + "\r\n");
                    }
                    if (rs.getInt("black") > 0) {
                        x += ("#bBlack Crystals - #r" + rs.getInt("black") + "\r\n");
                    }
                    if (rs.getInt("tear") > 0) {
                        x += ("#bIce Tears - #r" + rs.getInt("tear") + "\r\n");
                    }
                    if (rs.getInt("cloud") > 0) {
                        x += ("#bCloud Pieces - #r" + rs.getInt("cloud") + "\r\n");
                    }
                    if (rs.getInt("meso") > 0) {
                        x += ("#bMesos - #r" + rs.getInt("meso") + "\r\n");
                    }
                    if (rs.getInt("vp") > 0) {
                        x += ("#bVote Points - #r" + rs.getInt("vp") + "\r\n");
                    }
                    if (rs.getInt("quantity") > 0) {
                        x += ("#bQuantity - #r" + rs.getInt("quantity") + "\r\n");
                    }
                    if (x.equals("")) {
                        x = "#rFREE#k\r\n";
                    }
                    x += ("#bSeller - #r" + getNameById(rs.getInt("cid")) + "\r\n#dPress ok to buy it!");
                }
                ps.close();
                rs.close();
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getPrice(): " + e);
            }
            return "";
        }
    }

    public static boolean addItemAuction(int itemid, int quantity, int ice, int black, int tear, int cloud, int meso, int vp, int shards, MapleClient c) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO auctions " + "(`cid`, `type`, `quantity`, `ice`, `black`, `tear`, `cloud`, `meso`, `itemid`, `vp`, `shards`, `expirestamp`) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, 2);
                ps.setInt(3, quantity);
                ps.setInt(4, ice);
                ps.setInt(5, black);
                ps.setInt(6, tear);
                ps.setInt(7, cloud);
                ps.setInt(8, meso);
                ps.setInt(9, itemid);
                ps.setInt(10, vp);
                ps.setInt(11, shards);
                ps.setLong(12, (Calendar.getInstance().getTimeInMillis() + (604800000)));
                ps.execute();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println("Exception at addItemAuctions(): " + e);
            }
            return false;
        }
    }

    public static boolean addEquipAuction(int itemid, int str, int dex, int int_, int luk, int hp, int mp, int watk, int matk, int wdef, int mdef, int acc, int avoid, int hands, int speed, int jump, int locked, int vicious, int itemlevel, int itemexp, int ringid, int upgradeslots, int line1, int line2, int line3, int quantity, int ice, int black, int tear, int cloud, int meso, int vp, int shards, MapleClient c) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO auctions " + "(`cid`, `type`, `level`, `str`, `dex`, `int`, `luk`, `hp`, `mp`, `watk`, `matk`, `wdef`, `mdef`, `acc`, `avoid`, `hands`, `speed`, `jump`, `locked`, `vicious`, `itemlevel`, `itemexp`, `ringid`, `upgradeslots`, `line1`, `line2`, `line3`, `quantity`, `ice`, `black`, `tear`, `cloud`, `meso`, `itemid`, `vp`, `shards`, `expirestamp`) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, 1);
                ps.setInt(3, 999);
                ps.setInt(4, str);
                ps.setInt(5, dex);
                ps.setInt(6, int_);
                ps.setInt(7, luk);
                ps.setInt(8, hp);
                ps.setInt(9, mp);
                ps.setInt(10, watk);
                ps.setInt(11, matk);
                ps.setInt(12, wdef);
                ps.setInt(13, mdef);
                ps.setInt(14, acc);
                ps.setInt(15, avoid);
                ps.setInt(16, hands);
                ps.setInt(17, speed);
                ps.setInt(18, jump);
                ps.setInt(19, locked);
                ps.setInt(20, vicious);
                ps.setInt(21, itemlevel);
                ps.setInt(22, itemexp);
                ps.setInt(23, ringid);
                ps.setInt(24, upgradeslots);
                ps.setInt(25, line1);
                ps.setInt(26, line2);
                ps.setInt(27, line3);
                ps.setInt(28, quantity);
                ps.setInt(29, ice);
                ps.setInt(30, black);
                ps.setInt(31, tear);
                ps.setInt(32, cloud);
                ps.setInt(33, meso);
                ps.setInt(34, itemid);
                ps.setInt(35, vp);
                ps.setInt(36, shards);
                ps.setLong(37, (Calendar.getInstance().getTimeInMillis() + (604800000)));
                ps.execute();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println("Exception at addItemAuctions(): " + e);
            }
            return false;
        }
    }

    public static boolean addToDropbox(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    PreparedStatement ps2 = con.prepareStatement("INSERT INTO dropbox " + "(`auctionid`, `cid`, `ice`, `black`, `tear`, `cloud`, `meso`, `vp`, `shards`) VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps2.setInt(1, id);
                    ps2.setInt(2, rs.getInt("cid"));
                    ps2.setInt(3, rs.getInt("ice"));
                    ps2.setInt(4, rs.getInt("black"));
                    ps2.setInt(5, rs.getInt("tear"));
                    ps2.setInt(6, rs.getInt("cloud"));
                    ps2.setInt(7, rs.getInt("meso"));
                    ps2.setInt(8, rs.getInt("vp"));
                    ps2.setInt(9, rs.getInt("shards"));
                    ps2.executeUpdate();
                    ps2.close();
                }
                rs.close();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println("Exception at addToDropbox(): " + e);
            }
            return false;
        }
    }

    public static String getDropbox(MapleClient c) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT auctionid FROM dropbox WHERE cid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    x += "#L" + rs.getInt("auctionid") + "# Auction ID: #r" + rs.getInt("auctionid") + "#k#l";
                }
                rs.close();
                ps.close();
                if ("".equals(x)) {
                    x += "#L0#You have no auction income.#l";
                }
                return x;
            } catch (Exception e) {
                System.out.println("Exception at getDropbox(): " + e);
            }
            return "";
        }
    }

    public static boolean deleteAuction(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println("Exception at deleteAuctions(): " + e);
            }
            return false;
        }
    }

    public static String getNameById(int id) {
        return MapleCharacter.getNameById(id);
    }

    public static String showPoT(int x) {
        String y = "";
        switch (x) {
            case 0:
                break;
            case 1:
                y = "2% STR";
                break;
            case 2:
                y = "2% DEX";
                break;
            case 3:
                y = "2% LUK";
                break;
            case 4:
                y = "2% INT";
                break;
            case 5:
                y = "4% STR";
                break;
            case 6:
                y = "4% DEX";
                break;
            case 7:
                y = "4% LUK";
                break;
            case 8:
                y = "4% INT";
                break;
            case 9:
                y = "6% STR";
                break;
            case 10:
                y = "6% DEX";
                break;
            case 11:
                y = "6% LUK";
                break;
            case 12:
                y = "6% INT";
                break;
            case 13:
                y = "82 M.ATT";
                break;
            case 14:
                y = "76 W.ATT";
                break;
            case 15:
                y = "6% TOTAL DAMAGE";
                break;
            default:
                break;
        }
        return y;
    }

    public static boolean isExpired(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            boolean expire = false;
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT expirestamp FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (Calendar.getInstance().getTimeInMillis() >= rs.getLong("expirestamp")) {
                        expire = true;
                    }
                }
                rs.close();
                ps.close();
                return expire;
            } catch (Exception e) {
                System.out.println("Exception lols - " + e);
            }
            return expire;
        }
    }

    public static String searchItemAuction(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "#e#rAuction not found!";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT type FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("type") == 2) {
                        x = getItemInfo(id);
                    } else {
                        x = "The Auction Item has been found, but it is not an ITEM!";
                    }
                }
                rs.close();
                ps.close();
                return x;
            } catch (Exception e) {
            }
            return x;
        }
    }

    public static String searchEquipAuction(int id) {
        synchronized (Server.getInstance().AUCTIONQUEUE) {
            String x = "#e#rAuction not found!";
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT type FROM auctions WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("type") == 1) {
                        x = getEquipInfo(id);
                    } else {
                        x = "The Auction Item has been found, but it is not an EQUIP!";
                    }
                }
                rs.close();
                ps.close();
                return x;
            } catch (Exception e) {
            }
            return x;
        }
    }

    public static String getDropboxInfo(int auctionid) {
        String x = "#e#dAuction ID - " + auctionid + "\r\n";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM dropbox WHERE auctionid = ?");
            ps.setInt(1, auctionid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("shards") > 0) {
                    x += ("#bCrystal Shards - #r" + rs.getInt("shards") + "\r\n");
                }
                if (rs.getInt("ice") > 0) {
                    x += ("#bIce Pieces - #r" + rs.getInt("ice") + "\r\n");
                }
                if (rs.getInt("black") > 0) {
                    x += ("#bBlack Crystals - #r" + rs.getInt("black") + "\r\n");
                }
                if (rs.getInt("tear") > 0) {
                    x += ("#bIce Tears - #r" + rs.getInt("tear") + "\r\n");
                }
                if (rs.getInt("cloud") > 0) {
                    x += ("#bCloud Pieces - #r" + rs.getInt("cloud") + "\r\n");
                }
                if (rs.getInt("meso") > 0) {
                    x += ("#bMesos - #r" + rs.getInt("meso") + "\r\n");
                }
                if (rs.getInt("vp") > 0) {
                    x += ("#bVote Points - #r" + rs.getInt("vp") + "\r\n");
                }
            }
            if (x.equals("#e#dAuction ID - " + auctionid + "\r\n")) {
                x += "#rSOLD FOR FREE\r\n";
            }
            x += "#rWould you like to retrive your income now?";
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Exception at retriveDropbox(): " + e);
        }
        return x;
    }
}
